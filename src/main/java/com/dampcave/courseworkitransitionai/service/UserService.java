package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.models.People;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.PeopleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PeopleRepository peopleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PeopleRepository peopleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserRegistrationRepr userRegistrationRepr){
        User user = new User();
        People people = new People();
        user.setUsername(userRegistrationRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationRepr.getPassword()));
        people.setName(userRegistrationRepr.getName());
        people.setEmail(userRegistrationRepr.getEmail());
        people.setUser(user);
        peopleRepository.save(people);
    }
}
