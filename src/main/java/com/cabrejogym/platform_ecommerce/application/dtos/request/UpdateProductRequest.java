package com.cabrejogym.platform_ecommerce.application.dtos.request;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull @Positive
        BigDecimal price,

        @NotNull
        Boolean hasDiscount,

        @NotNull
        @DecimalMin(value = "0.00")
        @DecimalMax(value = "100.00")
        BigDecimal discountPercent,

        @NotNull @PositiveOrZero
        Integer stock
) {
}
