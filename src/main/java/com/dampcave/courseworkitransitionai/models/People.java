package com.dampcave.courseworkitransitionai.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "peoples")
@Data
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "registration")
    private Date registration;

    @Column(name = "lastlogin")
    private Date lastLogin;

    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private User user;

    public People() {
        this.registration = new Date();
    }
}
