package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.models.People;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.PeopleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PeopleRepository peopleRepository;


    @Autowired
    public UserService(UserRepository userRepository, PeopleRepository peopleRepository) {
        this.userRepository = userRepository;
        this.peopleRepository = peopleRepository;

    }

    public void create(UserRegistrationRepr userRegistrationRepr){
        User user = new User();
        People people = new People();

        people.setUser(user);
        peopleRepository.save(people);
    }
}
