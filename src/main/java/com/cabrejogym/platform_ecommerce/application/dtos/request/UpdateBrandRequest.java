package com.cabrejogym.platform_ecommerce.application.dtos.request;

public record UpdateBrandRequest(
        String name,
        String description,
        String logoUrl,
        Boolean active
) {}
