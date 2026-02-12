package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;

public record OrderTotalsDTO(
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal shipping,
        BigDecimal total
) {
}
