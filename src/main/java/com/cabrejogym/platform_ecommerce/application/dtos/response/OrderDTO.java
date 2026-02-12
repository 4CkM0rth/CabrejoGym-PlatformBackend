package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDTO(
        Long id,
        String orderNumber,
        String userEmail,
        OrderStatus status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal shipping,
        BigDecimal total,
        String couponCode,
        AddressDTO shippingAddress,
        AddressDTO billingAddress,
        Instant createdAt,
        List<OrderItemDTO> items
) {
}
