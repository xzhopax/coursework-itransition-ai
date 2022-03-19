package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
public class UserRegistrationRepr {

    private Long id;

    @NotBlank(message = " Please enter username ")
    @Length(max = 30, message = "username too long, max 30 characters ")
    private String username;


    @NotBlank(message = "Please enter password")
    @Length(max = 63, message = "password too long, max 63 characters ")
    private String password;

    @NotBlank(message = "Please enter repeat password")
    @Length(max = 63, message = "repeatPassword too long, max 63 characters ")
    private String repeatPassword;

    @Email(message = "Email not valid")
    @NotBlank(message = "Please enter email")
    @Length(max = 64, message = "email too long, max 64 characters ")
    private String email;

}
