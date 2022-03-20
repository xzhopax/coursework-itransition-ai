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

    public void deleteOverviewFromFilm(Film film){
        User user = userRepository.findByUsername(film.getAuthor().getUsername()).orElseThrow();
        user.getFilms().remove(film);
        userRepository.save(user);
    }

    public FormOverviewOnFilm editOverview(Film film) {
        FormOverviewOnFilm formFilm = new FormOverviewOnFilm();

        formFilm.setTitle(film.getTitle());
        formFilm.setLink(film.getUrlVideo());
        formFilm.setActors(parseObjectToStringThroughComma(film.getActors()));
        formFilm.setBudget(film.getBudget());
        formFilm.setDescription(film.getDescription());
        formFilm.setDuration(film.getDuration());
        formFilm.setProducers(parseObjectToStringThroughComma(film.getProducers()));
        formFilm.setRating(film.getRating());
        formFilm.setYear(film.getYear());

        return formFilm;
    }

    public void saveFilmOverview(FormOverviewOnFilm formFilm, Film film, User user) {
        if(storageService.checkUploadFile(formFilm.getPoster())) {
            if (film.getPicture() != null && !film.getPicture().isEmpty()){
                String oldPicture = user.getPhoto();
                storageService.deleteFile(oldPicture);
                new ResponseEntity<>(oldPicture, HttpStatus.OK);
            }
            String posterName = storageService.uploadFile(formFilm.getPoster());
            new ResponseEntity<>(posterName, HttpStatus.OK);
            film.setPicture(posterName);
        }

        film.setTitle(formFilm.getTitle());
        film.setUrlVideo(formFilm.getLink());
        film.setActors(addActors(formFilm.getActors()));
        film.setAuthor(user);
        film.setBudget(formFilm.getBudget());
        film.setDescription(formFilm.getDescription());
        film.setDuration(formFilm.getDuration());
        film.setProducers(addProducers(formFilm.getProducers()));
        film.setRating(formFilm.getRating());
        film.setYear(formFilm.getYear());

        filmRepository.save(film);
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

    public <T> String parseObjectToStringThroughComma(Set<T> objects){
        StringBuilder objectString = new StringBuilder();

        if (!objects.isEmpty()){
            for (T object : objects) {
                objectString.append(object).append(",");
            }
            if (objectString.length() > 0) {
                objectString.setLength(objectString.length() - 1);
            }
        }
        return objectString.toString();
    }

}
