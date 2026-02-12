package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.AddressType;

public record AddressDTO(
        Long id,
        AddressType type,
        String fullName,
        String street,
        String apartment,
        String city,
        String state,
        String zipCode,
        String country,
        String phone,
        Boolean isDefault
) {
}
