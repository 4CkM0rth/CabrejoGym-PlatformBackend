package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean hasDiscount,
        BigDecimal discountPercent,
        Integer stock
) {}
