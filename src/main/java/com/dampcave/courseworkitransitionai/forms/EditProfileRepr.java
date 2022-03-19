package com.dampcave.courseworkitransitionai.forms;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EditProfileRepr {

    private Long id;

    @Length(max = 30, message = "nickname too long, max 30 characters ")
    private String nickname;

    @Email(message = "Email should not be valid")
    @Length(max = 64, message = "email too long, max 64 characters ")
    private String email;

    @Length(max = 63, message = "password too long, max 63 characters ")
    private String password;

    @Length(max = 63, message = "password too long, max 63 characters ")
    private String newPassword;

    @Length(max = 63, message = "password too long, max 63 characters ")
    private String repeatPassword;



}
