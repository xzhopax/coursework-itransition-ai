package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class FormOverviewOnFilm {

    private Long id;

    private String title;

    private MultipartFile poster;

    private String link;

    private double rating;

    private int year;

    private int duration;

    private int budget;

    private String actors;

    private String producers;

    private String description;

}
