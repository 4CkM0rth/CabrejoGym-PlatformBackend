package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductVariantRequest(
        @NotBlank
        String sku,

        String variantName,

        String flavor,

        String size,

        String color,

        String weight,

        String material,

        String format,

        BigDecimal priceAdjustment,

        @NotNull @PositiveOrZero
        Integer stock
) {}
