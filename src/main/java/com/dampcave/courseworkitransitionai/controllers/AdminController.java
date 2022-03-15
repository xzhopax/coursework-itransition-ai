package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.AdminService;
import com.dampcave.courseworkitransitionai.service.CommentService;
import com.dampcave.courseworkitransitionai.service.FilmService;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@PreAuthorize("hasAuthority('ADMIN')")
@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final CommentService commentService;
    private final FilmService filmService;
    private final UserService userService;
    private final AdminService adminService;

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Autowired
    public AdminController(UserRepository userRepository,
                           FilmRepository filmRepository,
                           CommentService commentService,
                           FilmService filmService,
                           UserService userService, AdminService adminService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.commentService = commentService;
        this.filmService = filmService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/users/get-admin/{id}")
    public String addAdminOrRemoveAdmin(@PathVariable(value = "id") Long id) {
        adminService.addOrRemoveRoleAdmin(id);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showAllUser(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("title", "Users");
        return "profiles/users";
    }

    @GetMapping("/users/profile/{user}")
    public String profileIdAdmin(@PathVariable User user,
                            Model model){
        model.addAttribute("user", user);
        return "profiles/profile";
    }


    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        if (!adminService.hasRoleAdmin(user.getUsername())) {
            userRepository.delete(user);
        }
        return "redirect:/users";
    }

    @GetMapping("/deleteall")
    public String deleteAllUsers(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!adminService.hasRoleAdmin(user.getUsername())) {
                userRepository.delete(user);
            }
        }
        return "redirect:/users";
    }

    @GetMapping("/blocked")
    public String userBlockedAll(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!adminService.hasRoleAdmin(user.getUsername())) {
                user.setActive(false);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            userRepository.save(user);
        }
        return "redirect:/users";
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
        if (!adminService.hasRoleAdmin(user.getUsername())) {
            user.setActive(!user.isActive());
            userRepository.save(user);
        }
        return "redirect:/users";
    }


}
