package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductVariantRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductVariantRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductVariantDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.product.ProductVariantMapper;
import com.cabrejogym.platform_ecommerce.application.service.ProductVariantService;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.ProductVariant;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper variantMapper;

    @Override
    @Transactional
    public ProductVariantDTO createVariant(Long productId, CreateProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(request.sku());
        variant.setVariantName(request.variantName());
        variant.setFlavor(request.flavor());
        variant.setSize(request.size());
        variant.setColor(request.color());
        variant.setWeight(request.weight());
        variant.setMaterial(request.material());
        variant.setFormat(request.format());
        variant.setPriceAdjustment(request.priceAdjustment());
        variant.setStock(request.stock());
        variant.setActive(true);

        ProductVariant saved = variantRepository.save(variant);
        return variantMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ProductVariantDTO updateVariant(Long variantId, UpdateProductVariantRequest request) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (request.variantName() != null) variant.setVariantName(request.variantName());
        if (request.flavor() != null) variant.setFlavor(request.flavor());
        if (request.size() != null) variant.setSize(request.size());
        if (request.color() != null) variant.setColor(request.color());
        if (request.weight() != null) variant.setWeight(request.weight());
        if (request.material() != null) variant.setMaterial(request.material());
        if (request.format() != null) variant.setFormat(request.format());
        if (request.priceAdjustment() != null) variant.setPriceAdjustment(request.priceAdjustment());
        if (request.stock() != null) variant.setStock(request.stock());
        if (request.active() != null) variant.setActive(request.active());

        ProductVariant updated = variantRepository.save(variant);
        return variantMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteVariant(Long variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new RuntimeException("Variant not found");
        }
        variantRepository.deleteById(variantId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantDTO getVariantById(Long variantId) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));
        return variantMapper.toDTO(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantDTO getVariantBySku(String sku) {
        ProductVariant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Variant not found"));
        return variantMapper.toDTO(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> getVariantsByProductId(Long productId) {
        return variantMapper.toDTOList(variantRepository.findByProduct_IdAndActiveTrue(productId));
    }
}
