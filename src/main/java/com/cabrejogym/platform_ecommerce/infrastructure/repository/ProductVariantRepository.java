package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    
    List<ProductVariant> findByProductId(Long productId);
    
    List<ProductVariant> findByProduct_IdAndActiveTrue(Long productId);
    
    Optional<ProductVariant> findBySku(String sku);
    
    boolean existsBySku(String sku);
}
