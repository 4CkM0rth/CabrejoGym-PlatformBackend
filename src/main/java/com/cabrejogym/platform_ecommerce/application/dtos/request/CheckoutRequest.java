package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
        @NotNull
        Long shippingAddressId,
        @NotNull
        Long billingAddressId,
        String couponCode
) {
}
