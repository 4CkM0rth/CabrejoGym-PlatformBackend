package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull
        Long productId,
        @NotNull @Min(1)
        Integer quantity

) {
}
