package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationRepr {

    private Long id;

    @NotBlank(message = "Please enter username")
    @Size(min = 1, max = 30, message = " please enter name min = 1, max = 30 character")
    private String username;

    @NotBlank(message = "Please enter password")
    @NotEmpty(message = "Please enter password")
    @Size(min = 1, max = 30, message = " please enter password min = 1, max = 30 character")
    private String password;

    @NotBlank(message = "Please enter repeat password")
    @NotEmpty(message = "Please enter password")
    @Size(min = 1, max = 30, message = " please enter password min = 1, max = 30 character")
    private String repeatPassword;

    @Email(message = "Email should not be valid")
    @NotBlank(message = "Please enter email")
    private String email;

}
