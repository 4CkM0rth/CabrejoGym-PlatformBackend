package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductVariantRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductVariantRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductVariantDTO;

import java.util.List;

public interface ProductVariantService {
    ProductVariantDTO createVariant(Long productId, CreateProductVariantRequest request);
    ProductVariantDTO updateVariant(Long variantId, UpdateProductVariantRequest request);
    void deleteVariant(Long variantId);
    ProductVariantDTO getVariantById(Long variantId);
    ProductVariantDTO getVariantBySku(String sku);
    List<ProductVariantDTO> getVariantsByProductId(Long productId);
}
