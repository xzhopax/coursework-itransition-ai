package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EditProfileRepr {

    private Long id;

    private String nickname;

    @Email(message = "Email should not be valid")
    private String email;

    private String password;

    private String newPassword;

    private String repeatPassword;



}
