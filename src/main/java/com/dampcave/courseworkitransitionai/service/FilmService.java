package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.forms.FormOverviewOnFilm;
import com.dampcave.courseworkitransitionai.models.Actor;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.Producer;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.CommentRepository;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Service
public class FilmService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final CommentRepository commentRepository;
    private final StorageService storageService;

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Autowired
    public FilmService(UserRepository userRepository,
                       FilmRepository filmRepository,
                       CommentRepository commentRepository,
                       StorageService storageService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.commentRepository = commentRepository;
        this.storageService = storageService;
    }


    public Iterable<Film> findAllFilms() {
        return filmRepository.findAll();
    }


    public Set<Producer> addProducers(String producersString) {
        Set<Producer> producers = new HashSet<>();
        String[] strings = producersString.split(",");
        if (!producersString.trim().isEmpty()){
            for (String str : strings) {
                producers.add(new Producer(str));
            }
        }
        return producers;
    }

    public Set<Actor> addActors(String actorsString) {
        Set<Actor> actors = new HashSet<>();
        if (!actorsString.trim().isEmpty()) {
            String[] strings = actorsString.split(",");

            for (String str : strings) {
                actors.add(new Actor(str));
            }
        }
        return actors;
    }

    public void createFilmOverview(FormOverviewOnFilm formFilm) {

        String posterName = storageService.uploadFile(formFilm.getPoster());
        new ResponseEntity<>(posterName, HttpStatus.OK);

        Film film = new Film();
        film.setTitle(formFilm.getTitle());
        film.setUrlVideo(formFilm.getLink());
        film.setAuthor(userRepository.findByUsername(getAuth().getName()).orElseThrow());
        film.setActors(addActors(formFilm.getActors()));
        film.setPicture(posterName);
        film.setBudget(formFilm.getBudget());
        film.setDescription(formFilm.getDescription());
        film.setDuration(formFilm.getDuration());
        film.setProducers(addProducers(formFilm.getProducers()));
        film.setRating(formFilm.getRating());
        film.setYear(formFilm.getYear());

        filmRepository.save(film);
    }

    public void deleteOverviewFromFilm(long filmId){
        Film film = filmRepository.findById(filmId).orElseThrow();
        User user = userRepository.findByUsername(film.getAuthor().getUsername()).orElseThrow();
        user.getFilms().remove(film);
        userRepository.save(user);
    }

}
