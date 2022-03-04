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

import java.io.IOException;

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
    public String showAllFilms(Model model) {
        model.addAttribute("title", "all films");
        Iterable<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        model.addAttribute("path", uploadPath);
        return "films";
    }

    @GetMapping("/createfilm")
    public String getFormCreatFilm(Model model) {
        model.addAttribute("title", "Create Film");
        return "create-film";
    }


    @RequestMapping(value = "/createfilm", method = RequestMethod.POST)
    public String createNewFilms(@RequestParam(name = "filmname") String title,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "rating") int rating,
                                 @RequestParam(name = "year") int year,
                                 @RequestParam(value = "file") MultipartFile file,
                                 Model model) throws IOException {
        User user = userRepository.findByUsername(getAuth().getName()).orElseThrow();
        Film film = new Film(title, description, rating, year, user);
        String fileName = service.uploadFile(file);
        new ResponseEntity<>(fileName, HttpStatus.OK);

//        File uploadDir = new File(uploadPath);
//
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }
//
//        String uuidFile = UUID.randomUUID().toString();
//        String resultFilename = uuidFile + "." + file.getOriginalFilename();
//
//        if (!file.isEmpty()) {
//            byte[] bytes = file.getBytes();
//            File outFile = new File(uploadPath + resultFilename);
//            BufferedOutputStream stream =
//                    new BufferedOutputStream(new FileOutputStream(outFile));
//            stream.write(bytes);
//            stream.close();
//            file.transferTo(outFile);
//
//        }
        film.setPicture(fileName);
        filmRepository.save(film);

        Iterable<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        return "redirect:/films";
    }


    @PostMapping("/film/{id}/delete")
    public String deleteFilm(@PathVariable(value = "id") Long id,
                             Model model) {
        Film film = filmRepository.findById(id).orElseThrow();
        filmRepository.delete(film);
        return "redirect:/films";
    }

    @GetMapping("/film/{id}")
    public String getComment(@PathVariable(value = "id") Long id,
                             Model model) {

        Iterable<Comment> comments = commentRepository.findByFilm(filmRepository.findById(id).orElseThrow());
        model.addAttribute("comments", comments);
        model.addAttribute("username", getAuth().getName());
        model.addAttribute("title", "Main");
        model.addAttribute("film", filmRepository.findById(id).orElseThrow());
        model.addAttribute("id", id);
        model.addAttribute("path", uploadPath);

        return "film";
    }


    @RequestMapping(value = "/film/{id}", method = RequestMethod.POST)
    public String postComment(@PathVariable(value = "id") Long id,
                              @RequestParam(name = "textComment") String text,
                              Model model) {

        Film film = filmRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(getAuth().getName()).orElseThrow();
        Comment comment = new Comment(text, user, film);
        commentRepository.save(comment);
        Iterable<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        model.addAttribute("path", uploadPath);
        return "redirect:/films/film/{id}";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String postSearch(@RequestParam(name = "search-message") String search,
                             @RequestParam(name = "search-author") String author,
                             Model model) {
        Iterable<Comment> comments;

        if (search != null && !search.isEmpty()) {
            comments = commentRepository.findByMessage(search);
        } else {
            comments = commentRepository.findAll();
        }

        model.addAttribute("comments", comments);
        return "film";
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String fieldComments(Model model) {

        Iterable<Comment> comments = commentRepository.findAll();

        model.addAttribute("users-comment", comments);

        return "film";
    }
}
