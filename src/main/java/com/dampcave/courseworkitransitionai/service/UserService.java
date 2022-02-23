package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserRegistrationRepr userRegistrationRepr){
        User user = new User();
        user.setUsername(userRegistrationRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRepr.getPassword()));
        userRepository.save(user);
    }
}
