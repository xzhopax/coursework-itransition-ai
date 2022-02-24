package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.UserRegistrationRepr;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String showAllUser(Model model){
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("title", "Users");
        return "users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/users";
    }

    @GetMapping("/deleteall")
    public String deleteAllUsers() {
        userRepository.deleteAll();
        return "redirect:/users";
    }

    @GetMapping("/blocked")
    public String userBlockedAll(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setActive(false);
            userRepository.save(user);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
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
        user.setActive(!user.isActive());
        userRepository.save(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (user.getUsername().equals(auth.getName())) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "redirect:/login?logout";
        }
        return "redirect:/users";
    }



}
