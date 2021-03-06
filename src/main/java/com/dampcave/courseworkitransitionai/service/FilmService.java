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
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class FilmService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final StorageService storageService;
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public FilmService(UserRepository userRepository,
                       FilmRepository filmRepository,
                       StorageService storageService,
                       UserService userService, AdminService adminService) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.storageService = storageService;
        this.userService = userService;
        this.adminService = adminService;
    }

    public void deletePictureForFilm(String namePhoto){
        if (namePhoto != null && !namePhoto.isEmpty()) {
            storageService.deleteFile(namePhoto);
            new ResponseEntity<>(namePhoto, HttpStatus.OK);
        }
    }

    public void deleteOverviewFromFilm(Film film) {
        User user = userService.findUserByUsername(film.getAuthor().getUsername());
        deletePictureForFilm(film.getPicture());
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

    public void saveFilmOverview(FormOverviewOnFilm formFilm,
                                 Film film,
                                 User user) {
        if (storageService.checkUploadFile(formFilm.getPoster())) {
            deletePictureForFilm(film.getPicture());
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

    public boolean checkStringOnLengthAndCharter(String str){
        return str.matches("^[a-zA-Z ]*$") && str.length() < 255 ||
                str.matches("^[??-????-??????]*$") && str.length() < 255;
    }

    public Set<Producer> addProducers(String producersString) {
        Set<Producer> producers = new HashSet<>();
        String[] strings = producersString.split(",");
        if (!producersString.trim().isEmpty()) {
            for (String str : strings) {
                if (checkStringOnLengthAndCharter(str)) {
                    producers.add(new Producer(str));
                }
            }
        }
        return producers;
    }

    public Set<Actor> addActors(String actorsString) {
        Set<Actor> actors = new HashSet<>();
        if (!actorsString.trim().isEmpty()) {
            String[] strings = actorsString.split(",");
            for (String str : strings) {
                if (checkStringOnLengthAndCharter(str)) {
                    actors.add(new Actor(str));
                }
            }
        }
        return actors;
    }

    public <T> String parseObjectToStringThroughComma(Set<T> objects) {
        StringBuilder objectString = new StringBuilder();

        if (!objects.isEmpty()) {
            for (T object : objects) {
                objectString.append(object).append(",");
            }
            if (objectString.length() > 0) {
                objectString.setLength(objectString.length() - 1);
            }
        }
        return objectString.toString();
    }

    public List<Film> getAllFilms(){
        return filmRepository.findAll();
    }

    public Film getRandomFilm(){
        List<Film> films = getAllFilms();
        Random random = new Random();
        return films.get(random.nextInt(films.size()));
    }

    public Film getFilm(Optional<Film> film){
        return film.orElseGet(this::getRandomFilm);
    }

    public String saveCreateOverview(BindingResult bindingResult, FormOverviewOnFilm onFilm, User user){
        if (bindingResult.hasErrors()) {
            return "overviews/create-overview";
        }
        saveFilmOverview(onFilm, new Film(), user);

        return adminService.getViewIfHasRoleAdmin(userService.findUserByUsername(userService.getAuth().getName()),
                "redirect:/users",
                "redirect:/users/profile");
    }

    public String saveEditOverview(BindingResult bindingResult, FormOverviewOnFilm onFilm, Film film, User user){
        if (bindingResult.hasErrors()) {
            return "overviews/edit-overview";
        }
        saveFilmOverview(onFilm, film, user);
        return adminService.getViewIfHasRoleAdmin(userService.getAuthUser(),
                "redirect:/users",
                "redirect:/users/profile");
    }

}
