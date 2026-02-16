package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;

public record ProductVariantDTO(
        Long id,
        String sku,
        String variantName,
        String flavor,
        String size,
        String color,
        String weight,
        String material,
        String format,
        BigDecimal priceAdjustment,
        Integer stock,
        Boolean active
) {}
