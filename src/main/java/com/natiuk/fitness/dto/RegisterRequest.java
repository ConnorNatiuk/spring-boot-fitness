package com.natiuk.fitness.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class RegisterRequest {

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "Invalid must be at least 6 characters")
    private String password;

    private String firstName;
    private String lastName;


}
