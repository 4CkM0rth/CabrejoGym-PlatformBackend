package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateBranchRequest (
        @NotBlank @Size(max = 120)
        String name,

        @NotBlank @Size(max = 80)
        String city,

        @NotBlank @Size(max = 180)
        String address,

        @Size(max = 30)
        String phone,

        @Email @Size(max = 120)
        String email,

        @Size(max = 255)
        String openingHours,

        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        BigDecimal latitude,

        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        BigDecimal longitude
) {}
