package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class LoginController {

    @Autowired
    CommentRepository commentRepository;


    @GetMapping("/")
    public String getComment(@RequestParam(name = "name", required = false, defaultValue = "Damp Cave") String name,
                             Model model) {
        Iterable<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        model.addAttribute("name", name);
        return "main";
    }

    @GetMapping("/e")
    public String main() {
        return "home";
    }

    @PostMapping("/")
    public String postComment(@RequestParam(name = "text") String text,
                              @RequestParam(name = "author") String author,
                              Model model) {

        Comment comment = new Comment(text, author);
        commentRepository.save(comment);
        Iterable<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        return "main";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String postSearch(@RequestParam(name = "search-author") String search,
                             Model model) {
        Iterable<Comment> comments;

        if (search != null && !search.isEmpty()) {
            comments = commentRepository.findByAuthor(search);
        } else {
            comments = commentRepository.findAll();
        }

        model.addAttribute("comments", comments);
        return "main";
    }


}
