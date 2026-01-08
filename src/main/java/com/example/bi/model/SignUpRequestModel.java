package com.example.bi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestModel {
    @NotBlank
    private String username;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String role; // ADMIN, USER

    @NotBlank
    private String password;
}
