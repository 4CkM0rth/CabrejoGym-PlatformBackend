package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBrandRequest(
        @NotBlank
        String name,

        String description,

        String logoUrl
) {}
