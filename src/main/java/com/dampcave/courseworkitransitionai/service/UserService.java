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
import org.springframework.util.ObjectUtils;

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

        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello %s,\n"
                            + "Welcome to site Damp Cave.\n"
                            + "Please, for account activation visit next link: %s/activate/%s",
                    user.getUsername(),
                    urlSite,
                    user.getActivationCode());

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public void editProfile(User user, EditProfileRepr editProfileRepr) {
        System.out.println(editProfileRepr.getFile().toString());
        if (editProfileRepr.getFile() != null || !editProfileRepr.getFile().isEmpty()){
            System.out.println(editProfileRepr.getFile().getOriginalFilename());
            String fileName = storageService.uploadFile(editProfileRepr.getFile());
            new ResponseEntity<>(fileName, HttpStatus.OK);
            user.setPhoto(fileName);
        } else

        if(!editProfileRepr.getEmail().isEmpty()){
            user.setEmail(editProfileRepr.getEmail());
        }
        if (!editProfileRepr.getNickname().isEmpty()) {
            user.setNickname(editProfileRepr.getNickname());
        }
        if (!editProfileRepr.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(editProfileRepr.getPassword()));
        }
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
