package com.dampcave.courseworkitransitionai.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String message;

    private String author;

    public Comment(String message, String author) {
        this.message = message;
        this.author = author;
    }
}
