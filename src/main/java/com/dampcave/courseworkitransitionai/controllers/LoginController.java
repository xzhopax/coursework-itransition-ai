package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.UserLoginRepr;
import com.dampcave.courseworkitransitionai.forms.UserRegistrationRepr;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/")
    public String home( Model model) {
        model.addAttribute("title", "Home");
        return "home";
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


    @GetMapping("/test")
    public String test( Model model) {
        model.addAttribute("title", "Test");
        return "test";
    }

    @GetMapping("/dark-theme")
    public void setDarkTheme(HttpServletResponse response) {
        Cookie cookie = new Cookie("theme", "navbar-dark bg-dark");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/light-theme")
    public void setLightTheme(HttpServletResponse response) {
        Cookie cookie = new Cookie("theme", "navbar-light bg-light");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
