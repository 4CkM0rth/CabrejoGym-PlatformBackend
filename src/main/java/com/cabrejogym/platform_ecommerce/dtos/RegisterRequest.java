package com.cabrejogym.platform_ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank @Size(max = 100)
        String nombre,

        @NotBlank @Size(max = 100)
        String apellido,

        @NotBlank @Email @Size(max = 150)
        String email,

        @NotBlank @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres")
        String password,

        @Past
        LocalDate fechaNacimiento
) {
}
