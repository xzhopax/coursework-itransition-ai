package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.forms.EditProfileRepr;
import com.dampcave.courseworkitransitionai.forms.UserRegistrationRepr;
import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.RoleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final AdminService adminService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       StorageService storageService, AdminService adminService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
        this.adminService = adminService;
    }

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean checkUserInBD(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void create(UserRegistrationRepr userRegistrationRepr) {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getById(1L));
        user.setRoles(roles);
        user.setActive(true);
        user.setUsername(userRegistrationRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRepr.getPassword()));
        user.setEmail(userRegistrationRepr.getEmail());
        user.setNickname("NoNameNPC" + user.getId());
        userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User getAuthUser() {
        return userRepository.findByUsername(getAuth().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String registrationAction(UserRegistrationRepr userRegistrationRepr, BindingResult bindingResult) {
        if (userRegistrationRepr.getPassword() != null
                && !userRegistrationRepr.getPassword().equals(userRegistrationRepr.getRepeatPassword())) {
            bindingResult.rejectValue("password", "", " passwords not equals ");
        }
        if (checkUserInBD(userRegistrationRepr.getUsername())) {
            bindingResult.rejectValue("username", "", " User exist ");
        }
        if (bindingResult.hasErrors()) {
            return "security/register";
        } else {
            create(userRegistrationRepr);
            return "redirect:/login";
        }
    }

    public String editProfile(User user, EditProfileRepr editProfileRepr, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profiles/edit-profile";
        } else {
            if (!editProfileRepr.getEmail().isEmpty() && !editProfileRepr.getEmail().equals(user.getEmail())) {
                user.setEmail(editProfileRepr.getEmail());
            }
            if (!editProfileRepr.getNickname().isEmpty()) {
                user.setNickname(editProfileRepr.getNickname());
            }
            if (!editProfileRepr.getPassword().isEmpty() && user.getPassword().equals(editProfileRepr.getPassword())) {
                if (!editProfileRepr.getNewPassword().isEmpty() && !editProfileRepr.getRepeatPassword().isEmpty()) {
                    if (editProfileRepr.getNewPassword().equals(editProfileRepr.getRepeatPassword())) {
                        user.setPassword(passwordEncoder.encode(editProfileRepr.getNewPassword()));
                    }
                }
            }
            userRepository.save(user);
            return adminService.getViewIfHasRoleAdmin(findUserByUsername(getAuth().getName()),
                    "redirect:/users/",
                    "redirect:/users/profile");
        }
    }

    public String editPhoto(MultipartFile file, User user) {
        if (!storageService.checkUploadFile(file)) {
            return "profiles/edit-photo";
        } else {
            if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                String oldPhoto = user.getPhoto();
                storageService.deleteFile(oldPhoto);
                new ResponseEntity<>(oldPhoto, HttpStatus.OK);
            }
            String fileName = storageService.uploadFile(file);
            new ResponseEntity<>(fileName, HttpStatus.OK);
            user.setPhoto(fileName);
            userRepository.save(user);
            return adminService.getViewIfHasRoleAdmin(findUserByUsername(getAuth().getName()),
                    "redirect:/users/",
                    "redirect:/users/profile");
        }
    }
}
