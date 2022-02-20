package com.dampcave.courseworkitransitionai.service;


import com.dampcave.courseworkitransitionai.repositoryes.PeopleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public UserAuthService(UserRepository userRepository, PeopleRepository peopleRepository) {
        this.userRepository = userRepository;
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.dampcave.courseworkitransitionai.models.User> myUser = userRepository.findByUsername(username);
        if (myUser.get().isActive()) {
            myUser.get().getPeople().setLastLogin(new Date());
            peopleRepository.save(myUser.get().getPeople());
            return userRepository.findByUsername(username)
                    .map(user -> new User(
                            user.getUsername(),
                            user.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("USER"))
                    )).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else
            throw new UsernameNotFoundException("User blocked");
    }
}
