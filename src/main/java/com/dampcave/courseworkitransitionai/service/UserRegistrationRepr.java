package com.dampcave.courseworkitransitionai.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationRepr {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;

//    @NotBlank
//    private String name;
//
//    @NotBlank
//    private String email;
}
