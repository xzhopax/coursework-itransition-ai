package com.dampcave.courseworkitransitionai.repositoryes;

import com.dampcave.courseworkitransitionai.models.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment>findByAuthor(String author);
}
