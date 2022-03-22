package com.dampcave.courseworkitransitionai.repositoryes;


import com.dampcave.courseworkitransitionai.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}
