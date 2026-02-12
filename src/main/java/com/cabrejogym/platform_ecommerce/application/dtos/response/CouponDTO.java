package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.CouponType;

import java.math.BigDecimal;
import java.time.Instant;

public record CouponDTO(
        Long id,
        String code,
        CouponType type,
        BigDecimal value,
        BigDecimal minPurchase,
        BigDecimal maxDiscount,
        Integer usageLimit,
        Integer usageCount,
        Instant validFrom,
        Instant validUntil,
        Boolean active
) {
}
