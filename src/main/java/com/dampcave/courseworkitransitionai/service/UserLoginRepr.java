package com.dampcave.courseworkitransitionai.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRepr {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
