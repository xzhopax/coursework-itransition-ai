package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.EditProfileRepr;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
public class UserController {

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("user", userService.findUserByUsername(getAuth().getName()));
        return "profiles/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @GetMapping("/profile/{user}/edit")
    public String editProfile(@PathVariable User user,
                              @ModelAttribute("edit") EditProfileRepr editProfileRepr,
                              Model model) {
        model.addAttribute("user", user);
        return "profiles/edit-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @RequestMapping(value = "/profile/{user}/edit", method = RequestMethod.POST)
    public String confirmEditProfile(@PathVariable User user,
                                     @Valid EditProfileRepr editProfileRepr,
                                     BindingResult bindingResult) {
        return userService.editProfile(user,editProfileRepr,bindingResult);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @GetMapping("/profile/{user}/photo")
    public String editPhoto(@PathVariable User user,
                            Model model) {
        model.addAttribute("user", user);
        return "profiles/edit-photo";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @RequestMapping(value = "/profile/{user}/photo", method = RequestMethod.POST)
    public String updatePhoto(@PathVariable User user,
                              @RequestParam(value = "file") MultipartFile file) {
       return userService.editPhoto(file,user);
    }
}
