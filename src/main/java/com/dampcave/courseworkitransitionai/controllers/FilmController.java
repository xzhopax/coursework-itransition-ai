package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.FormOverviewOnFilm;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller()
@RequestMapping("/films")
public class FilmController {
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final CommentService commentService;
    private final FilmService filmService;
    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public FilmController(UserRepository userRepository,
                          FilmRepository filmRepository,
                          CommentService commentService,
                          FilmService filmService,
                          AdminService adminService,
                          UserService userService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.commentService = commentService;
        this.filmService = filmService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping()
    public String showAllOverviewFilms( Model model) {
        User user = adminService.ifAnonymousOrAuthentication();
        model.addAttribute("title", "All Films");
        model.addAttribute("films", filmRepository.findAll());
        model.addAttribute("user", user);
        return "overviews/films";
    }

    @GetMapping("/film/{film}")
    public String getPageOverview(@PathVariable Film film,
                                  Model model) {
        model.addAttribute("title", "Film Overview");
        model.addAttribute("film", film);
        model.addAttribute("comments", commentService.getAllCommentsFromFilm(film));
        return "overviews/film";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @RequestMapping(value = "/film/{film}", method = RequestMethod.POST)
    public String actionsInOverviewFilm(@PathVariable Film film,
                                        @RequestParam(name = "textComment") String message) {
        commentService.sendComment(message, film, getAuth().getName());
        return "redirect:/films/film/{film}";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @GetMapping("/user/{user}/create-overview")
    public String getFormCreatOverview(@PathVariable User user,
                                       Model model) {
        FormOverviewOnFilm film = new FormOverviewOnFilm();
        model.addAttribute("user", user);
        model.addAttribute("film", film);
        return "overviews/create-overview";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @RequestMapping(value = "/user/{user}/create-overview", method = RequestMethod.POST)
    public String pushFormCreatingOverview(@PathVariable User user,
                                           FormOverviewOnFilm onFilm,
                                           BindingResult bindingResult) {
        filmService.createFilmOverview(onFilm, user);
        if (adminService.hasRoleAdmin(getAuth().getName()))
            return "redirect:/users/";
        else
            return "redirect:/users/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #film.author.username.equals(authentication.name)")
    @GetMapping("/film/delete/{film}")
    public String deleteFilm(@PathVariable Film film) {
        filmService.deleteOverviewFromFilm(film);
        if (adminService.hasRoleAdmin(getAuth().getName()))
            return "redirect:/users/";
        else
            return "redirect:/users/profile";
    }
}
