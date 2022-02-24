package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.Comment;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/films")
public class FilmController {

    UserRepository userRepository;
    CommentRepository commentRepository;
    FilmRepository filmRepository;

    @Autowired
    public FilmController(UserRepository userRepository, CommentRepository commentRepository, FilmRepository filmRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.filmRepository = filmRepository;
    }

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping()
    public String showAllFilms(Model model){
        model.addAttribute("title", "all films");
        Iterable<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        return "films";
    }

    @GetMapping("/createfilm")
    public String getFormCreatFilm(Model model){
        model.addAttribute("title", "Create Film");
        return "create-film";
    }

    @RequestMapping(value = "/createfilm", method = RequestMethod.POST)
    public String createNewFilms(@RequestParam(name = "filmname") String title,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "rating") int rating,
                                 Model model) {
        User user = userRepository.findByUsername(getAuth().getName()).orElseThrow();
        Film film = new Film(title,description,rating,user);
        filmRepository.save(film);
        Iterable<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        return "redirect:/films";
    }


    @PostMapping("/{id}/delete")
    public String deleteFilm(@PathVariable(value = "id") Long id, Model model) {
        Film film = filmRepository.findById(id).orElseThrow();
        filmRepository.delete(film);
        return "redirect:/films";
    }
}
