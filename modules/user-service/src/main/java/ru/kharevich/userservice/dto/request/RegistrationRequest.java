package ru.kharevich.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest (

        @NotBlank(message = "username is mandatory")
        String username,

        @NotBlank(message = "firstname is mandatory")
        String firstname,

        @NotBlank(message = "lastname is mandatory")
        String lastname,

        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "password is mandatory")
        String password

){
}
