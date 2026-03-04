package com.cabrejogym.platform_ecommerce.application.dtos.request;

import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;

import java.math.BigDecimal;
import java.util.List;

public record ProductSearchCriteria(
        String query,
        Long categoryId,
        Long brandId,
        List<Long> tagIds,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean hasDiscount,
        Boolean inStock,
        PublicationStatus status,
        String sortBy,  // price, name, createdAt, discount
        String sortDirection  // asc, desc
) {
    public ProductSearchCriteria {
        // Defaults
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }
        if (sortDirection == null || sortDirection.isBlank()) {
            sortDirection = "desc";
        }
    }
}
