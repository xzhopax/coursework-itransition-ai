package com.dampcave.courseworkitransitionai.models;




public class User{


    private Long id;


    private String username;


    private String password;


    private People people;

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }
}
