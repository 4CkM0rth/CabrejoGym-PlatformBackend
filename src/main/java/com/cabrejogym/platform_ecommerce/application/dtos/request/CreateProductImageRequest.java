package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProductImageRequest(
        @NotBlank
        String url,

        String altText,

        Integer displayOrder,

        Boolean isPrimary
) {}
