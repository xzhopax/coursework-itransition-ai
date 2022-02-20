package com.dampcave.courseworkitransitionai.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TINYINT")
    private boolean active;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private People people;

    public User() {
        this.active = true;
    }
}
