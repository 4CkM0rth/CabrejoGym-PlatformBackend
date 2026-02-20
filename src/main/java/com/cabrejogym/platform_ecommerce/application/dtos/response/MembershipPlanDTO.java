package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;

public record MembershipPlanDTO(
        Long id,
        Long branchId,
        String name,
        String description,
        BigDecimal price,
        Integer durationMonths,
        Boolean isPopular,
        Boolean active,
        Integer displayOrder
) {}
