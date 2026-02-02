package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRefundRequest(
        @NotNull
        Long orderId,
        @NotBlank
        String reason
) {
}
