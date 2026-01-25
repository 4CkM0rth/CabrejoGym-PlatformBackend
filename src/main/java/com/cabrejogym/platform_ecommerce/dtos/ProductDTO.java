package com.cabrejogym.platform_ecommerce.dtos;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean hasDiscount,
        BigDecimal discountPercent
) {}
