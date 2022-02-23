package com.dampcave.courseworkitransitionai.repositoryes;


import com.dampcave.courseworkitransitionai.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean findByActiveTrue();

    boolean findByActiveFalse();
}
