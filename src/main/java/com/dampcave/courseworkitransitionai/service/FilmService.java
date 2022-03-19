package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.forms.FormOverviewOnFilm;
import com.dampcave.courseworkitransitionai.models.Actor;
import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.Producer;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FilmService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final StorageService storageService;

    @Autowired
    public FilmService(UserRepository userRepository,
                       FilmRepository filmRepository,
                       StorageService storageService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.storageService = storageService;
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

    public void createFilmOverview(FormOverviewOnFilm formFilm, User user) {
        Film film = new Film();
        if(storageService.checkUploadFile(formFilm.getPoster())) {
            String posterName = storageService.uploadFile(formFilm.getPoster());
            new ResponseEntity<>(posterName, HttpStatus.OK);
            film.setPicture(posterName);
        }

        film.setTitle(formFilm.getTitle());
        film.setUrlVideo(formFilm.getLink());
        film.setAuthor(user);
        film.setActors(addActors(formFilm.getActors()));

        film.setBudget(formFilm.getBudget());
        film.setDescription(formFilm.getDescription());
        film.setDuration(formFilm.getDuration());
        film.setProducers(addProducers(formFilm.getProducers()));
        film.setRating(formFilm.getRating());
        film.setYear(formFilm.getYear());

        filmRepository.save(film);
    }

    public void deleteOverviewFromFilm(Film film){
        User user = userRepository.findByUsername(film.getAuthor().getUsername()).orElseThrow();
        user.getFilms().remove(film);
        userRepository.save(user);
    }

}
