package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.ReviewStatus;

import java.time.Instant;

public record BranchReviewDTO(
        Long id,
        Long branchId,
        Long userId,
        String userName,
        Integer rating,
        String comment,
        ReviewStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
