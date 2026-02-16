package com.cabrejogym.platform_ecommerce.application.dtos.response;

public record BrandDTO(
        Long id,
        String name,
        String slug,
        String description,
        String logoUrl,
        Boolean active
) {}
