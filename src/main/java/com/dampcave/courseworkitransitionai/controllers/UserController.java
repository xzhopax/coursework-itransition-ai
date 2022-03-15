package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.EditProfileRepr;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.*;
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

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final CommentService commentService;
    private final FilmService filmService;
    private final UserService userService;
    private final StorageService storageService;
    private final AdminService adminService;

    @Autowired
    public UserController(UserRepository userRepository, FilmRepository filmRepository, CommentService commentService, FilmService filmService, UserService userService, StorageService storageService, AdminService adminService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.commentService = commentService;
        this.filmService = filmService;
        this.userService = userService;
        this.storageService = storageService;
        this.adminService = adminService;
    }

    @GetMapping("/profile")
    public String profile(Model model){
        User user = userRepository.findByUsername(getAuth().getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        return "profiles/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @GetMapping("/profile/{user}/edit")
    public String editProfile(@PathVariable User user,
                              Model model){
        EditProfileRepr editProfileRepr = new EditProfileRepr();
        model.addAttribute("edit", editProfileRepr);
        model.addAttribute("user", user);
        return "profiles/edit-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @RequestMapping(value = "/profile/{user}/edit", method = RequestMethod.POST)
    public String confirmEditProfile(@PathVariable User user,
                                     @Valid EditProfileRepr editProfileRepr,
                                     BindingResult bindingResult){
        userService.editProfile(user,editProfileRepr);
        if (adminService.hasRoleAdmin(getAuth().getName()))
        return "redirect:/users/";
        else
        return "redirect:/users/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @GetMapping("/profile/{user}/photo")
    public String editPhoto(@PathVariable User user,
                                          Model model){
        model.addAttribute("user", user);
        return "profiles/edit-photo";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @RequestMapping(value = "/profile/{user}/photo", method = RequestMethod.POST)
    public String updatePhoto(@PathVariable User user,
                              @RequestParam(value = "file") MultipartFile file ){
        userService.editPhoto(file,user);
        if (adminService.hasRoleAdmin(getAuth().getName()))
            return "redirect:/users/";
        else
            return "redirect:/users/profile";
    }


}
