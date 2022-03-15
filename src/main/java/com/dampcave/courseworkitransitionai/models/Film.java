package com.dampcave.courseworkitransitionai.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "Please fill title")
    private String title;
    private String picture;
    private String urlVideo;
    @NotBlank(message = "Please fill description")
    @Length(max = 2048, message = "Description too long, max 2048 characters ")
    private String description;
    @Min(value =0, message = "should be greater than 0")
    private int duration;
    @Min(value =0, message = "should be greater than 0")
    private double rating;
    @NotBlank(message = "Please year release")
    @Min(value =0, message = "should be greater than 0")
    private int year;
    @Min(value =0, message = "should be greater than 0")
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


}
