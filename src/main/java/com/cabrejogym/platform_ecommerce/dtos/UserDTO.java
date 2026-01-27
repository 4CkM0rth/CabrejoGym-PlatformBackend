package com.cabrejogym.platform_ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UserDTO(
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @Email
        @NotBlank
        String email,
        LocalDate birthDate
) {}
