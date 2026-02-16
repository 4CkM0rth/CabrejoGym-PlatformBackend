package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    
    List<ProductImage> findByProductIdOrderByDisplayOrder(Long productId);
    
    List<ProductImage> findByProductId(Long productId);
}
