package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;
import java.util.List;

public record CartDTO(
        Long id,
        List<CartItemDTO> items,
        Integer totalItems,
        BigDecimal subtotal
) {
}
