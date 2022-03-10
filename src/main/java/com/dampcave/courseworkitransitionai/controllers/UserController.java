package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.UserRegistrationRepr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
public class UserController {

    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String showAllUser(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("title", "Users");
        return "users";
    }
    @GetMapping("/profile")
    public String profile(Model model){
//        User user = userRepository.findByUsername(getCurrentUser().getName()).orElseThrow();
//        model.addAttribute("user", user);
        return "profile";
    }
    @GetMapping("/profile/{user}/edit")
    public String editProfile(@PathVariable User user,
                          Model model){
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/{user}/edit")
    public String confirmEditProfile(@PathVariable User user,
                              Model model){
        model.addAttribute("user", user);
        return "redirect:/profile";
    }


    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        if (!user.getUsername().equals("admin")) {
                userRepository.delete(user);
        }
        return "redirect:/users";
    }

    @GetMapping("/deleteall")
    public String deleteAllUsers(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getUsername().equals("admin")) {
                userRepository.delete(user);
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/login?logout";
    }

    @GetMapping("/blocked")
    public String userBlockedAll(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getUsername().equals("admin")) {
                user.setActive(false);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            userRepository.save(user);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/unblocked")
    public String userUnblockedAll() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setActive(true);
            userRepository.save(user);
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/{id}/isactive", method = RequestMethod.GET)
    public String userIsActive(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id).orElseThrow();
        if (!user.getUsername().equals("admin")) {
            user.setActive(!user.isActive());
            userRepository.save(user);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (user.getUsername().equals(auth.getName())) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                return "redirect:/login?logout";
            }
        }
        return "redirect:/users";
    }


}
