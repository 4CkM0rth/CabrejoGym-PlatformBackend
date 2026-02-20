package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBranchAmenityRequest(
        @NotBlank
        String name,
        String description,
        String iconName,
        Boolean available
) {}
