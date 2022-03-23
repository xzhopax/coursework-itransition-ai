package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
public class FormOverviewOnFilm {

    private Long id;

    @NotBlank(message = "Please fill title")
    @Length(max = 255, message = "Producers too long, max 255 characters ")
    private String title;

    private MultipartFile poster;

    @URL(message = "URL not valid")
    @Length(max = 255, message = "link too long, max 255 characters ")
    private String link;

    @Range(min=0, max=10, message = "rating may be min=0, max=10")
    private double rating;


    @Range(min=0, max=2147483647, message = "year may be min=0, max=2147483647")
    private int year;

    @Range(min=0, max=2147483647, message = "duration may be min=0, max=2147483647")
    private int duration;

    @Range(min=0, max=9223372036854775807L, message = "duration may be min=0, max=9223372036854775807")
    private long budget;

    @Length(max = 2048, message = "Actors too long, max 2048 characters ")
    private String actors;

    @Length(max = 255, message = "Producers too long, max 255 characters ")
    private String producers;

    @NotBlank(message = "Please fill description")
    @Length(max = 2048, message = "Description too long, max 2048 characters ")
    private String description;

}
