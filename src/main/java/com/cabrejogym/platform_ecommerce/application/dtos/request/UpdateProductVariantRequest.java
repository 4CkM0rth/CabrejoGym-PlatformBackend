package com.cabrejogym.platform_ecommerce.application.dtos.request;

import java.math.BigDecimal;

public record UpdateProductVariantRequest(
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
