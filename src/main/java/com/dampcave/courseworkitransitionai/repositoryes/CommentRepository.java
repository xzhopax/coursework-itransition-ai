package com.dampcave.courseworkitransitionai.repositoryes;

import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<User> findByAuthor(User author);
    List<Comment> findByFilm(Film film);
    List<Comment> findByMessage(String message);
}
