package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.UserLoginRepr;
import com.dampcave.courseworkitransitionai.forms.UserRegistrationRepr;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
public class LoginController {

    private final UserService userService;


    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginUser( @ModelAttribute("user") UserLoginRepr userLoginRepr) {
        return "security/login";
    }


    @GetMapping("/register")
    public String registrationUserGet(@ModelAttribute("user") UserRegistrationRepr userRegistrationRepr ){
        return "security/register";
    }

    @PostMapping("/register")
    public String registrationNewUserPost(
            @ModelAttribute("user")  @Valid UserRegistrationRepr userRegistrationRepr,
                                            BindingResult bindingResult){
       return userService.registrationAction(userRegistrationRepr,bindingResult);
    }
}
