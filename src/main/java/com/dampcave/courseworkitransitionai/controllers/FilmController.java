package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.forms.EditProfileRepr;
import com.dampcave.courseworkitransitionai.forms.FormOverviewOnFilm;
import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.CommentService;
import com.dampcave.courseworkitransitionai.service.FilmService;
import com.dampcave.courseworkitransitionai.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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

    @Autowired
    public FilmController(UserRepository userRepository,
                          FilmRepository filmRepository,
                          CommentService commentService,
                          FilmService filmService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.commentService = commentService;
        this.filmService = filmService;
    }

    @GetMapping()
    public String showAllOverviewFilms(Model model) {
        model.addAttribute("title", "All Films");
        model.addAttribute("films", filmService.findAllFilms());
        return "films";
    }

    @GetMapping("/film/{id}")
    public String getPageOverview(@PathVariable(value = "id") Long id,
                                                Model model) {
        model.addAttribute("title", "Film Overview");
        model.addAttribute("film", filmRepository.findById(id).orElseThrow());
        model.addAttribute("comments", commentService.getAllCommentsFromFilm(id));
        model.addAttribute("username", getAuth().getName());
        return "film";
    }

    @RequestMapping(value = "/film/{id}", method = RequestMethod.POST)
    public String actionsInOverviewFilm(@PathVariable(value = "id") Long id,
                                        @RequestParam(name = "textComment") String message) {
        commentService.sendComment(message, id, getAuth().getName());
        return "redirect:/films/film/{id}";
    }

    @GetMapping("/create-overview")
    public String getFormCreatOverview(Model model) {
        FormOverviewOnFilm film = new FormOverviewOnFilm();
        model.addAttribute("film", film);
        return "create-overview";
    }

    @RequestMapping(value = "/create-overview", method = RequestMethod.POST)
    public String pushFormCreatingOverview(@Valid FormOverviewOnFilm onFilm,
                                           BindingResult bindingResult) {
        filmService.createFilmOverview(onFilm);
        return "redirect:/films";
    }

    @PostMapping("/film/delete/{id}")
    public String deleteFilm(@PathVariable(value = "id") Long id) {
        filmService.deleteOverviewFromFilm(id);
        return "redirect:/films";
    }
}
