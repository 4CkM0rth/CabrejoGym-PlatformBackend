package com.cabrejogym.platform_ecommerce.application.dtos.response;

import java.util.List;

public record CategoryDTO(
        Long id,
        String name,
        String slug,
        String description,
        Long parentId,
        List<CategoryDTO> children,
        Integer displayOrder,
        Boolean active
) {}
