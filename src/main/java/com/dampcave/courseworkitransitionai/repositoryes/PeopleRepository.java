package com.dampcave.courseworkitransitionai.repositoryes;


import com.dampcave.courseworkitransitionai.models.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PeopleRepository extends JpaRepository<People, Long> {
    Optional<People> findByUserUsername( String username);
}
