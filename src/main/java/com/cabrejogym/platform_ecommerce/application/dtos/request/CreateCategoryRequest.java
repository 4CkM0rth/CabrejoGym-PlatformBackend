package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank
        String name,

        String description,

        Long parentId,

        Integer displayOrder
) {}
