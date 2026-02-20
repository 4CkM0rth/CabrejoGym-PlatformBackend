package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record BranchDTO(
        Long id,
        String name,
        String city,
        String address,
        String phone,
        String email,
        String openingHours,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer capacity,
        Integer areaSqm,
        Boolean active,
        Instant createdAt,
        List<BranchImageDTO> images,
        List<BranchAmenityDTO> amenities,
        List<MembershipPlanDTO> membershipPlans,
        Double averageRating,
        Long totalReviews
) {}
