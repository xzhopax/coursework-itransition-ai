package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.repositoryes.PeopleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public UserAuthService(UserRepository userRepository, PeopleRepository peopleRepository) {
        this.userRepository = userRepository;
        this.peopleRepository = peopleRepository;
    }


}
