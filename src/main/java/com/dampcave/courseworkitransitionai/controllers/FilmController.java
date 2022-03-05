package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller()
@RequestMapping("/films")
public class FilmController {

    @Value("${upload.path}")
    private String uploadPath;

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FilmRepository filmRepository;
    private final StorageService service;

    @Autowired
    public FilmController(UserRepository userRepository,
                          CommentRepository commentRepository,
                          FilmRepository filmRepository,
                          StorageService service) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.filmRepository = filmRepository;
        this.service = service;
    }

    @GetMapping()
    public String showAllOverviewFilms(Model model) {
        Iterable<Film> films = filmRepository.findAll();
        model.addAttribute("title", "All Films");
        model.addAttribute("films", films);
        return "films";
    }

    @GetMapping("/film/{id}")
    public String getPageOverview(@PathVariable(value = "id") Long id,
                                  Model model) {
        Iterable<Comment> comments = commentRepository.findByFilm(filmRepository.findById(id).orElseThrow());
        model.addAttribute("title", "Film Overview");
        model.addAttribute("film", filmRepository.findById(id).orElseThrow());
        model.addAttribute("comments", comments);
        model.addAttribute("username", getAuth().getName());
        return "film";
    }

    @RequestMapping(value = "/film/{id}", method = RequestMethod.POST)
    public String actionsInOverviewFilm(@PathVariable(value = "id") Long id,
                                        @RequestParam(name = "textComment") String text) {
        Film film = filmRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(getAuth().getName()).orElseThrow();
        Comment comment = new Comment(text, user, film);
        commentRepository.save(comment);
        return "redirect:/films/film/{id}";
    }

    @GetMapping("/create-overview")
    public String getFormCreatOverview(Model model) {
        model.addAttribute("title", "Create Film");
        return "create-overview";
    }

    @RequestMapping(value = "/create-overview", method = RequestMethod.POST)
    public String pushFormCreatingOverview(@RequestParam(name = "filmname") String title,
                                           @RequestParam(name = "description") String description,
                                           @RequestParam(name = "rating") int rating,
                                           @RequestParam(name = "year") int year,
                                           @RequestParam(value = "file") MultipartFile file) {
        User user = userRepository.findByUsername(getAuth().getName()).orElseThrow();
        Film film = new Film(title, description, rating, year, user);
        String fileName = service.uploadFile(file);
        new ResponseEntity<>(fileName, HttpStatus.OK);
        film.setPicture(fileName);
        filmRepository.save(film);
        return "redirect:/films";
    }

    @PostMapping("/film/delete/{id}")
    public String deleteFilm(@PathVariable(value = "id") Long id) {
        Film film = filmRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(film.getAuthor().getUsername()).orElseThrow();
        user.getFilms().remove(film);
        userRepository.save(user);
        return "redirect:/films";
    }
}
