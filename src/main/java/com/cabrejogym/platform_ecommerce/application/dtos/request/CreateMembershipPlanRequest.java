package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMembershipPlanRequest(
        @NotBlank
        String name,
        String description,
        @NotNull @Positive
        BigDecimal price,
        @NotNull @Min(1)
        Integer durationMonths,
        Boolean isPopular,
        Integer displayOrder
) {}
