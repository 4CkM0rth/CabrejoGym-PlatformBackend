package com.cabrejogym.platform_ecommerce.application.dtos.response;

import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductDTO(
        Long id,
        String name,
        String slug,
        String description,
        String shortDescription,
        BigDecimal price,
        Boolean hasDiscount,
        BigDecimal discountPercent,
        Integer stock,
        CategoryDTO category,
        BrandDTO brand,
        PublicationStatus status,
        Boolean hasVariants,
        List<ProductVariantDTO> variants,
        List<ProductImageDTO> images,
        List<TagDTO> tags,
        Instant createdAt,
        Instant updatedAt
) {}
