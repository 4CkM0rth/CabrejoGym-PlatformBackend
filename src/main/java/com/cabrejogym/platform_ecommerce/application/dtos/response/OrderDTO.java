package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDTO(
        Long id,
        String userEmail,
        OrderStatus status,
        BigDecimal total,
        Instant createdAt,
        List<OrderItemDTO> items
) {
}
