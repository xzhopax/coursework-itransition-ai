package com.dampcave.courseworkitransitionai;

import com.dampcave.courseworkitransitionai.models.Actor;
import com.dampcave.courseworkitransitionai.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;


@SpringBootApplication
public class CourseworkItransitionAiApplication {


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(CourseworkItransitionAiApplication.class, args);

    }




}
