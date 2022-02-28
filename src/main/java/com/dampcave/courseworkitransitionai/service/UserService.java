package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.RoleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void create(UserRegistrationRepr userRegistrationRepr){
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getById(1L));
        user.setRoles(roles);
        user.setUsername(userRegistrationRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRepr.getPassword()));
        userRepository.save(user);
    }
}
