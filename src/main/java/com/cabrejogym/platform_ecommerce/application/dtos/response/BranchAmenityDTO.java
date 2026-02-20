package com.cabrejogym.platform_ecommerce.application.dtos.response;

public record BranchAmenityDTO(
        Long id,
        Long branchId,
        String name,
        String description,
        String iconName,
        Boolean available
) {}
