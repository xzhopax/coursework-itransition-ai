package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.FormOverviewOnFilm;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.service.AdminService;
import com.dampcave.courseworkitransitionai.service.CommentService;
import com.dampcave.courseworkitransitionai.service.FilmService;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller()
@RequestMapping("/films")
public class FilmController {
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private final CommentService commentService;
    private final FilmService filmService;
    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public FilmController(CommentService commentService,
                          FilmService filmService,
                          AdminService adminService,
                          UserService userService) {
        this.commentService = commentService;
        this.filmService = filmService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping()
    public String showAllOverviewFilms(Model model) {
        model.addAttribute("films", filmService.getAllFilms());
        return "overviews/films";
    }

    @GetMapping("/film/{film}")
    public String getPageOverview(@PathVariable Optional<Film> film,
                                  Model model) {
        model.addAttribute("film", filmService.getFilm(film));
        model.addAttribute("comments", commentService.getAllCommentsFromFilm(film.get()));
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
                                       @ModelAttribute("film") FormOverviewOnFilm film,
                                       Model model) {
        model.addAttribute("user", user);

        return "overviews/create-overview";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @RequestMapping(value = "/user/{user}/create-overview", method = RequestMethod.POST)
    public String pushFormCreatingOverview(@ModelAttribute("film") @Valid FormOverviewOnFilm onFilm,
                                           BindingResult bindingResult,
                                           @PathVariable User user) {
        return filmService.saveCreateOverview(bindingResult,onFilm,user);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #film.author.username.equals(authentication.name)")
    @GetMapping("/film/delete/{film}")
    public String deleteFilm(@PathVariable Film film) {

        filmService.deleteOverviewFromFilm(film);
        return adminService.getViewIfHasRoleAdmin(userService.findUserByUsername(getAuth().getName()),
                "redirect:/users",
                "redirect:/users/profile");
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @GetMapping("/user/{user}/edit-overview/{film}")
    public String getEditFormOverview(@PathVariable User user,
                                      @PathVariable Film film,
                                      Model model) {
        FormOverviewOnFilm formFilm = filmService.editOverview(film);
        model.addAttribute("formFilm", formFilm);
        model.addAttribute("film", film);
        model.addAttribute("user", user);
        return "overviews/edit-overview";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.username.equals(authentication.name)")
    @PostMapping("/user/{user}/edit-overview/{film}")
    public String saveEditFormOverview(@ModelAttribute("formFilm") @Valid FormOverviewOnFilm formFilm,
                                       BindingResult bindingResult,
                                       @PathVariable User user,
                                       @PathVariable Film film) {

       return filmService.saveEditOverview(bindingResult,formFilm,film,user);

    }
}