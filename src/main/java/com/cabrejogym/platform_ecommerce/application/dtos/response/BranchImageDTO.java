package com.cabrejogym.platform_ecommerce.application.dtos.response;

public record BranchImageDTO(
        Long id,
        Long branchId,
        String url,
        String altText,
        Integer displayOrder,
        Boolean isPrimary
) {}
