package com.dampcave.courseworkitransitionai.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String message;
    private String filename;
    private String time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id")
    private Film film;


    public Comment(String message, Film film, User author,String time) {
        this.message = message;
        this.film = film;
        this.time = time;
        this.author = author;
    }
}
