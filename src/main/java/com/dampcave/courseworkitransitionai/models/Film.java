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
    @Column(columnDefinition="text")
    private String description;
    private int duration;
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
               joinColumns = @JoinColumn(name = "film_id", referencedColumnName="id"),
               inverseJoinColumns=@JoinColumn(name="producer_id", referencedColumnName="id") )
    private Set<Producer> producers;

    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "film_actor",
            schema = "public",
            joinColumns = @JoinColumn(name = "film_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="actor_id", referencedColumnName="id") )
    private Set<Actor> actors;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval=false)
    @JoinColumn(name = "film_id")
    private List<Comment> comments;


}
