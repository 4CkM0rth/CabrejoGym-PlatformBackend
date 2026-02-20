package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMembershipPlanRequest(
        String name,
        String description,
        @Positive
        BigDecimal price,
        @Min(1)
        Integer durationMonths,
        Boolean isPopular,
        Boolean active,
        Integer displayOrder
) {}
