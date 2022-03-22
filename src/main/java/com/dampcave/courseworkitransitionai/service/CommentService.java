package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(UserRepository userRepository,
                          CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Iterable<Comment> getAllCommentsFromFilm(Film film) {
        return commentRepository.findByFilm(film);
    }

    public String getTimeString() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public void sendComment(String message, Film film, String author) {
        User user = userRepository.findByUsername(author).orElseThrow();
        Comment comment = new Comment(message, film, user, getTimeString());
        commentRepository.save(comment);
    }

}
