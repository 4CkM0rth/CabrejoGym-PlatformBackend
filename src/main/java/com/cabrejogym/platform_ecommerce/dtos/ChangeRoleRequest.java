package com.cabrejogym.platform_ecommerce.dtos;

import com.cabrejogym.platform_ecommerce.model.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
        @NotNull(message = "El rol es obligatorio")
        Role role
) {
}
