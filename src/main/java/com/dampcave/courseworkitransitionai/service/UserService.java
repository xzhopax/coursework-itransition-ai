package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.forms.EditProfileRepr;
import com.dampcave.courseworkitransitionai.forms.UserRegistrationRepr;
import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.RoleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final StorageService storageService;

    @Value("${site.url}")
    private String urlSite;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       MailSender mailSender,
                       StorageService storageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.storageService = storageService;
    }

    public boolean checkUserInBD(User user) {
        return userRepository.findByUsername(user.getUsername()).isEmpty();
    }

    public void create(UserRegistrationRepr userRegistrationRepr) {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getById(1L));
        user.setRoles(roles);
        user.setActive(false);
        user.setUsername(userRegistrationRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRepr.getPassword()));
        user.setEmail(userRegistrationRepr.getEmail());
        user.setActivationCode(UUID.randomUUID().toString());
        user.setNickname("NoNameNPC" + user.getId());
        userRepository.save(user);

        mailSender.messageActivation(user);
    }

    public void editProfile(User user, EditProfileRepr editProfileRepr) {

        if(!editProfileRepr.getEmail().isEmpty()){
            user.setEmail(editProfileRepr.getEmail());
        }
        if (!editProfileRepr.getNickname().isEmpty()) {
            user.setNickname(editProfileRepr.getNickname());
        }
        if (!editProfileRepr.getPassword().isEmpty() && user.getPassword().equals(editProfileRepr.getPassword())) {
            if (!editProfileRepr.getNewPassword().isEmpty() && !editProfileRepr.getRepeatPassword().isEmpty()){
                if (editProfileRepr.getNewPassword().equals(editProfileRepr.getRepeatPassword())){
                    user.setPassword(passwordEncoder.encode(editProfileRepr.getNewPassword()));
                }
            }
        }
        userRepository.save(user);
    }

    public void editPhoto(MultipartFile file, User user){

            String fileName = storageService.uploadFile(file);
            new ResponseEntity<>(fileName, HttpStatus.OK);
            user.setPhoto(fileName);
            userRepository.save(user);

    }

    public boolean isActivateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    public void autoGenerateNickname(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setNickname("NoNameNPC" + user.getId());
        userRepository.save(user);
    }


}
