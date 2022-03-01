package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.amazon.S3Util;
import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/")
    public String home( Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

    @GetMapping("/test")
    public String test( Model model) {
        model.addAttribute("title", "Home");
        return "test";
    }

    @GetMapping("/upload")
    public String upload( Model model) {
        model.addAttribute("title", "Upload");
        return "upload";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name = "description") String description,
                             @RequestParam(name = "file")MultipartFile multipartFile,
                             Model model) {
        String filename = multipartFile.getOriginalFilename();
        System.out.println("Description: " + description);
        System.out.println("Filename: " + filename);
        String message;
        try {
            S3Util.uploadFile(filename,multipartFile.getInputStream());
            message ="Your file upload";
        } catch (IOException e) {
             message ="Your file upload" + e.getMessage();
        }


        model.addAttribute("title", "Upload");
        model.addAttribute("message", message);
        return "message";
    }


    @GetMapping("/main")
    public String getComment(Model model) {
        model.addAttribute("title", "Main");

        return "main";
    }


}
