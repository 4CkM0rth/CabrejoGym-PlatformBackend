package com.cabrejogym.platform_ecommerce.application.dtos.request;

import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,

        String description,

        String shortDescription,

        @Positive
        BigDecimal price,

        Boolean hasDiscount,

        @DecimalMin(value = "0.00")
        @DecimalMax(value = "100.00")
        BigDecimal discountPercent,

        @PositiveOrZero
        Integer stock,

        Long categoryId,

        Long brandId,

        PublicationStatus status,

        Boolean hasVariants
) {
}
