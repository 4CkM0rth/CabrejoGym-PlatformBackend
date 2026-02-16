package com.cabrejogym.platform_ecommerce.application.dtos.request;

import com.cabrejogym.platform_ecommerce.domain.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAddressRequest(
        @NotNull
        AddressType type,
        @NotBlank
        String fullName,
        @NotBlank
        String street,
        String apartment,
        @NotBlank
        String city,
        @NotBlank
        String state,
        @NotBlank
        @Pattern(regexp = "^[0-9]{4,10}(-[0-9]{1,4})?$", message = "Código postal inválido")
        String zipCode,
        @NotBlank
        String country,
        @NotBlank
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Teléfono inválido")
        String phone,
        Boolean isDefault
) {
}
