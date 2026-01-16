package com.cabrejogym.platform_ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MeUpdateRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
        String apellido,

        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate fechaNacimiento
) {
}
