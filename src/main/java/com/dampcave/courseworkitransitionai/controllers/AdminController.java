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
@RequestMapping("/users")
@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final CommentService commentService;
    private final FilmService filmService;
    private final UserService userService;
    private final AdminService adminService;

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

    @GetMapping("/get-admin/{id}")
    public String addAdminOrRemoveAdmin(@PathVariable(value = "id") Long id) {
        adminService.addOrRemoveRoleAdmin(id);
        return "redirect:/users";
    }


    @GetMapping()
    public String showAllUser(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("title", "Users");
        return "profiles/users";
    }


    @GetMapping("/profile/{id}")
    public String profileIdAdmin(@PathVariable Long id,
                            Model model){
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "profiles/profile";
    }

    @GetMapping("{user}/film/{film}")
    public String getPageOverviewAdmin(@PathVariable() Film film,
                                       @PathVariable() User user,
                                  Model model) {
        model.addAttribute("title", "Film Overview");
        model.addAttribute("user", user);
        model.addAttribute("film", film);
        model.addAttribute("comments", commentService.getAllCommentsFromFilm(film.getId()));
        return "overviews/film";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "{user}/film/{film}", method = RequestMethod.POST)
    public String actionsInOverviewFilmAdmin(@PathVariable Film film,
                                        @PathVariable User user,
                                        @RequestParam(name = "textComment") String message) {
        commentService.sendComment(message, film, user.getUsername());
        return "redirect:/films/{user}/film/{film}";
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
