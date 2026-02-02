package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record RefundDTO (
    Long id,
    Long orderId,
    String userEmail,
    RefundStatus status,
    BigDecimal amount,
    String reason,
    Instant requestedAt,
    Instant resolvedAt,
    String adminNote
) {}
