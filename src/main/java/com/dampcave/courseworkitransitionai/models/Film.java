package com.dampcave.courseworkitransitionai.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;
    private String picture;
    private String urlVideo;
    private String description;
    private double rating;
    private int year;
    private String producer;

//    @ManyToMany( fetch = FetchType.EAGER)
//    @JoinTable(name = "film_genres", joinColumns = @JoinColumn(name = "genres_id"))
//    private Set<Genre> genres;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval=false)
    @JoinColumn(name = "film_id")
    private List<Comment> comments;

    public Film(String title, String description, int rating, int year, User author) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.year = year;
        this.author = author;
    }
}
