package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.RoleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private MailSender mailSender;

    @Value("${site.url}")
    private String urlSite;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       MailSender mailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public boolean checkUserInBD(User user){
        return userRepository.findByUsername(user.getUsername()).isEmpty();
    }

    public void create(UserRegistrationRepr userRegistrationRepr){
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getById(1L));
        user.setRoles(roles);
        user.setActive(false);
        user.setUsername(userRegistrationRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRepr.getPassword()));
        user.setEmail(userRegistrationRepr.getEmail());
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
}
