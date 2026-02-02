package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateBranchRequest(
        @NotBlank @Size(max = 120)
        String name,
        @NotBlank @Size(max = 80)
        String city,
        @NotBlank @Size(max = 180)
        String address,
        @Size(max = 30)
        String phone,
        @Size(max = 120)
        String email,
        @Size(max = 255)
        String openingHours,
        BigDecimal latitude,
        BigDecimal longitude
) {}
