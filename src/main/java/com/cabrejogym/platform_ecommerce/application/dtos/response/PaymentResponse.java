package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.PaymentMethod;
import com.cabrejogym.platform_ecommerce.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long paymentId,
        Long orderId,
        String orderNumber,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        BigDecimal amount,
        String transactionId,
        String authorizationCode,
        String message,
        Instant createdAt
) {}
