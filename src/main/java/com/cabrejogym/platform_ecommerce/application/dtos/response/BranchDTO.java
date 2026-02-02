package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;
import java.time.Instant;

public record BranchDTO(
        Long id,
        String name,
        String city,
        String address,
        String phone,
        String email,
        String openingHours,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean active,
        Instant createdAt

) {}
