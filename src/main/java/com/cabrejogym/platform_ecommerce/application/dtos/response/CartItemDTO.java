package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;

public record CartItemDTO(
        Long id,
        Long productId,
        String productName,
        BigDecimal productPrice,
        Integer quantity,
        BigDecimal lineTotal
) {
}
