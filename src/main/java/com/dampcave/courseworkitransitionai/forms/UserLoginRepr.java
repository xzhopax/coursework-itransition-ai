package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class UserLoginRepr {

    private Long id;

    @NotBlank(message = " Please enter username ")
    @Length(max = 30, message = "username too long, max 30 characters ")
    @NotNull
    private String username;

    @NotBlank(message = " Please enter password ")
    @Length(max = 63, message = "password too long, max 63 characters ")
    @NotNull
    private String password;
}
