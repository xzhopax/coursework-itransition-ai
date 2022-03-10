package com.dampcave.courseworkitransitionai.service;

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

    public void createFilmOverview(String titleFilm,
                                   String descriptionFilm,
                                   double ratingFilm,
                                   String authorName,
                                   String linkVideoTrailer,
                                   String duration,
                                   int year,
                                   long budget,
                                   MultipartFile poster,
                                   String actors,
                                   String producers) {
        User user = userRepository.findByUsername(authorName).orElseThrow();
        String fileName = storageService.uploadFile(poster);
        new ResponseEntity<>(fileName, HttpStatus.OK);

        Film film = new Film(titleFilm,
                             linkVideoTrailer,
                             descriptionFilm,
                             duration,
                             ratingFilm,
                             year,
                             budget,
                             user,
                             addProducers(producers),
                             addActors(actors));

        film.setPicture(fileName);
        filmRepository.save(film);
    }

    public void deleteOverviewFromFilm(long filmId){
        Film film = filmRepository.findById(filmId).orElseThrow();
        User user = userRepository.findByUsername(film.getAuthor().getUsername()).orElseThrow();
        user.getFilms().remove(film);
        userRepository.save(user);
    }

    public String getTimeMovie(int hour, int minutes){
        return String.format("%d:%d", hour,minutes);
    }

}
