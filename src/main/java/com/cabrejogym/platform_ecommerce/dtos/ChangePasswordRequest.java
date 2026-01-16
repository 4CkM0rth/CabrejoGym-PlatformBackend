package com.cabrejogym.platform_ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "La contraseña actual es obligatoria")
        String actualPassword,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 6, max = 72, message = "La nueva contraseña debe tener entre 6 y 72 caracteres")
        String newPassword
) {
}
