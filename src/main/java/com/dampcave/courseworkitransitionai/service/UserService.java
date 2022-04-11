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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final AdminService adminService;
    private final MailSender mailSender;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       StorageService storageService, AdminService adminService, MailSender mailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
        this.adminService = adminService;
        this.mailSender = mailSender;
    }

    @Value("${site.url}")
    private String urlSite;

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
        user.setNickname("NoNameNPC");
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        if (!ObjectUtils.isEmpty(user.getEmail())){
            String message = String.format("Hello %s,\n"
                            + "Welcome to Damp Cave.\n"
                            + "Please, visit next link: %s/activate/%s",
                    user.getUsername(),
                    urlSite,
                    user.getActivationCode());

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean isActivateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null){return false;}

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return true;
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
        if (!validUsername(userRegistrationRepr.getUsername())) {
            bindingResult.rejectValue("username", "username not valid", "username not valid");
            bindingResult.rejectValue("username",
                    "a-zA-Z0-9!@#$%_ and max length 30",
                    "a-zA-Z0-9!@#$%_ and max length 30");
        }
        if (!validPassword(userRegistrationRepr.getPassword())) {
            bindingResult.rejectValue("password", "passwords not valid", "passwords not valid");
            bindingResult.rejectValue("password",
                    "a-zA-Z0-9!@#$%^ &* and max length 63",
                    "a-zA-Z0-9!@#$%^ &* and max length 63");
        }
        if (!checkPasswords(userRegistrationRepr.getPassword(), userRegistrationRepr.getRepeatPassword())) {
            bindingResult.rejectValue("password", "passwords not equals", " passwords not equals");
        }
        if (checkUserInBD(userRegistrationRepr.getUsername())) {
            bindingResult.rejectValue("username", "User exist", " User exist ");
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
            if (!editProfileRepr.getNickname().isEmpty() && validName(editProfileRepr.getNickname())) {
                user.setNickname(editProfileRepr.getNickname());
            }
            if (checkPasswords(user,
                    editProfileRepr.getPassword(),
                    editProfileRepr.getNewPassword(),
                    editProfileRepr.getRepeatPassword())) {
                user.setPassword(passwordEncoder.encode(editProfileRepr.getNewPassword()));
            }

            userRepository.save(user);
            return adminService.getViewIfHasRoleAdmin(findUserByUsername(getAuth().getName()),
                    "redirect:/users",
                    "redirect:/users/profile");
        }
    }

    public boolean validPassword(String password) {
        return password.matches("^[a-zA-Z0-9!@#$%^ &*]*$") && password.length() < 63;
    }

    public boolean checkPasswords(String password1, String password2) {
        if (!password1.isBlank() && !password2.isBlank()) {
            return password1.equals(password2);
        }
        return false;
    }

    public boolean checkPasswords(User user, String password, String newPassword, String repeatPassword) {
        if (!password.isEmpty() && user.getPassword().equals(password)) {
            if (!newPassword.isEmpty() && !repeatPassword.isEmpty()) {
                if (validPassword(newPassword) && validPassword(repeatPassword)) {
                    return newPassword.equals(repeatPassword);
                }
            }
        }
        return false;
    }

    public boolean validUsername(String name) {
        return name.matches("^[a-zA-Z0-9!@#$%_]*$") && name.length() < 30;
    }

    public boolean validName(String name) {
        return name.matches("^[a-zA-Z ]*$") && name.length() < 30 ||
                name.matches("^[а-яА-ЯЁё ]*$") && name.length() < 30;
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
                    "redirect:/users",
                    "redirect:/users/profile");
        }
    }
}
