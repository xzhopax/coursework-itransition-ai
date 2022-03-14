package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.FormOverviewOnFilm;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.AdminService;
import com.dampcave.courseworkitransitionai.service.CommentService;
import com.dampcave.courseworkitransitionai.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public FilmController(UserRepository userRepository,
                          FilmRepository filmRepository,
                          CommentService commentService,
                          FilmService filmService,
                          AdminService adminService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.commentService = commentService;
        this.filmService = filmService;
        this.adminService = adminService;
    }

    @GetMapping()
    public String showAllOverviewFilms(Model model) {
        model.addAttribute("title", "All Films");
        model.addAttribute("films", filmRepository.findAll());
        return "overviews/films";
    }

    @GetMapping("/film/{id}")
    public String getPageOverview(@PathVariable(value = "id") Long id,
                                  Model model) {
        Film film = filmRepository.findById(id).orElseThrow();
        model.addAttribute("title", "Film Overview");
        model.addAttribute("film", film);
        model.addAttribute("comments", commentService.getAllCommentsFromFilm(id));
        return "overviews/film";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #film.author.username.equals(authentication.name)")
    @RequestMapping(value = "/film/{film}", method = RequestMethod.POST)
    public String actionsInOverviewFilm(@PathVariable Film film,
                                        @RequestParam(name = "textComment") String message) {
        commentService.sendComment(message, film, getAuth().getName());
        return "redirect:/films/film/{id}";
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
        return "redirect:/users/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #film.author.username.equals(authentication.name)")
    @GetMapping("/film/delete/{film}")
    public String deleteFilm(@PathVariable Film film) {
        filmService.deleteOverviewFromFilm(film);
    return "redirect:/users/profile";
    }
}
