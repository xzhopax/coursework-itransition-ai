package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.UserLoginRepr;
import com.dampcave.courseworkitransitionai.forms.UserRegistrationRepr;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
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
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public LoginController(UserService userService, CommentRepository commentRepository, UserRepository userRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/")
    public String home( Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }
    @GetMapping("/403")
    public String error() {
        return "/errors/error";
    }

    @GetMapping("/login")
    public String loginUser( @ModelAttribute("user") @Valid UserLoginRepr userLoginRepr,BindingResult bindingResult) {
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
        if (userRegistrationRepr.getPassword() == null || userRegistrationRepr.getRepeatPassword() == null ){
            bindingResult.rejectValue("password","", "passwords not be null");
            bindingResult.rejectValue("repeatPassword","", "passwords not be null");

           return "security/register";
        }
        if (userRegistrationRepr.getUsername() == null || userRegistrationRepr.getUsername().isEmpty()){
            bindingResult.rejectValue("username","", "username not be null");
            return "security/register";
        }
        if (userRegistrationRepr.getEmail() == null || userRegistrationRepr.getEmail().isEmpty()){
            bindingResult.rejectValue("username","", "email not be null");
            return "security/register";
        }

        if (userRegistrationRepr.getPassword() != null
                && !userRegistrationRepr.getPassword().equals(userRegistrationRepr.getRepeatPassword())){
            bindingResult.rejectValue("password","", "passwords not equals");
        }

        if (userService.checkUserInBD(userRegistrationRepr.getUsername())){
            bindingResult.rejectValue("username","","");
        }
        if (bindingResult.hasErrors()){
            return "security/register";
        } else {
//            userService.create(userRegistrationRepr);
//            userService.autoGenerateNickname(userRegistrationRepr.getUsername());
            return "redirect:/login";
        }
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
