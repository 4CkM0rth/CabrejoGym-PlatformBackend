package com.cabrejogym.platform_ecommerce.application.dtos.response;

public record ProductImageDTO(
        Long id,
        String url,
        String altText,
        Integer displayOrder,
        Boolean isPrimary
) {}
