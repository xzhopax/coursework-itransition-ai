package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.UserLoginRepr;
import com.dampcave.courseworkitransitionai.service.UserRegistrationRepr;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class LoginController {

    private final UserService userService;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public LoginController(UserService userService, CommentRepository commentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginUser( Model model) {
        UserLoginRepr userLoginRepr = new UserLoginRepr();
        model.addAttribute("user", userLoginRepr);
        model.addAttribute("title", "Login");
        return "login";
    }

    @GetMapping("/register")
    public String registrationUserGet(Model model){
        model.addAttribute("title", "Registration");
        UserRegistrationRepr userRegistrationRepr = new UserRegistrationRepr();
        model.addAttribute("user", userRegistrationRepr);
        return "register";
    }

    @PostMapping("/register")
    public String registrationNewUserPost(
            @Valid UserRegistrationRepr userRegistrationRepr,
            BindingResult bindingResult){
//        User user = userRepository.findByUsername(userRegistrationRepr.getUsername()).orElseThrow();
//
//        if (bindingResult.hasErrors()){
//            return "register";
//        }
//        if (user != null){
//            bindingResult.rejectValue("username","","User exists!");
//            return "register";
//        }
//
//        if (!userRegistrationRepr.getPassword().equals(userRegistrationRepr.getRepeatPassword())){
//            bindingResult.rejectValue("password","", "passwords not equals");
//            return "register";
//        }

        userService.create(userRegistrationRepr);
        return "redirect:/login";
    }





}
