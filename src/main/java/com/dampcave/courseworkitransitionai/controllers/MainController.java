package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {


    UserRepository userRepository;


    CommentRepository commentRepository;

    @Autowired
    public MainController(UserRepository userRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/")
    public String home( Model model) {
        model.addAttribute("title", "Main");
        return "home";
    }


    @GetMapping("/main")
    public String getComment(@RequestParam(name = "name", required = false, defaultValue = "Damp Cave") String name,
                             Model model) {
        Iterable<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        model.addAttribute("name", name);
        model.addAttribute("title", name);

        return "main";
    }



    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public String postComment( @RequestParam(name = "textComment") String text,
                               Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow() ;
        Comment comment = new Comment(text, user);
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

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String fieldComments(Model model) {

        Iterable<Comment> comments = commentRepository.findAll();

        model.addAttribute("users-comment", comments);

        return "main";
    }
}
