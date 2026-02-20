package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBranchReviewRequest(
        @NotNull
        @Min(1)
        @Max(5)
        Integer rating,
        String comment
) {}
