package com.cabrejogym.platform_ecommerce.application.dtos.request;

public record UpdateCategoryRequest(
        String name,
        String description,
        Long parentId,
        Integer displayOrder,
        Boolean active
) {}
