package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLoginRepr {

    private Long id;

    @NotBlank(message = "Please enter username")
    @Size(min = 1, max = 40, message = "1 and 40 characters")
    private String username;

    @NotBlank(message = "Please enter password")
    private String password;
}
