package com.example.bi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestModel {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
