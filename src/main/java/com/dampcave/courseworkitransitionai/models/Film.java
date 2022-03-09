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
    private long budget;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "film_genre", joinColumns = @JoinColumn(name = "genre_id"))
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "film_producer",
               schema = "public",
               joinColumns = @JoinColumn(name = "producer_id", referencedColumnName="id"),
               inverseJoinColumns=@JoinColumn(name="film_id", referencedColumnName="id") )
    private Set<Producer> producers;

    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "film_actor",
            schema = "public",
            joinColumns = @JoinColumn(name = "actor_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="film_id", referencedColumnName="id") )
    private Set<Actor> actors;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval=false)
    @JoinColumn(name = "film_id")
    private List<Comment> comments;

    public Film(String title,
                String urlVideo,
                String description,
                double rating,
                int year,
                long budget,
                User author,
                Set<Producer> producers,
                Set<Actor> actors) {
        this.title = title;
        this.urlVideo = urlVideo;
        this.description = description;
        this.rating = rating;
        this.year = year;
        this.budget = budget;
        this.author = author;
        this.producers = producers;
        this.actors = actors;
    }

//    public Film(String title, String description, double rating, int year, User author) {
//        this.title = title;
//        this.description = description;
//        this.rating = rating;
//        this.year = year;
//        this.author = author;
//    }


//    public Film(String title,
//                String picture,
//                String urlVideo,
//                String description,
//                double rating,
//                int year,
//                long budget,
//                Set<Genre> genres,
//                User author,
//                Set<Producer> producers,
//                Set<Actor> actors,
//                List<Comment> comments) {
//        this.title = title;
//        this.picture = picture;
//        this.urlVideo = urlVideo;
//        this.description = description;
//        this.rating = rating;
//        this.year = year;
//        this.budget = budget;
//        this.genres = genres;
//        this.author = author;
//        this.producers = producers;
//        this.actors = actors;
//        this.comments = comments;
//    }
}
