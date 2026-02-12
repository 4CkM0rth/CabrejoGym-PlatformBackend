package com.cabrejogym.platform_ecommerce.application.dtos.request;

import com.cabrejogym.platform_ecommerce.domain.enums.CouponType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Z0-9_-]{3,20}$", message = "Código debe ser 3-20 caracteres alfanuméricos en mayúsculas")
        String code,
        @NotNull
        CouponType type,
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal value,
        @DecimalMin(value = "0.00")
        BigDecimal minPurchase,
        @DecimalMin(value = "0.00")
        BigDecimal maxDiscount,
        @Min(1)
        Integer usageLimit,
        @NotNull
        Instant validFrom,
        @NotNull
        Instant validUntil
) {
}
