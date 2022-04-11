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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String home( Model model) {
        model.addAttribute("user", userService.getAuthUser());
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

    @GetMapping("/activate/{code}")
    public String activate( @PathVariable String code,
                            Model model){
        boolean isActivated = userService.isActivateUser(code);

        if (isActivated){
            model.addAttribute("message", "User activated");
        } else {
            model.addAttribute("message", "Activation code not found");
        }

        return "message";
    }
}
