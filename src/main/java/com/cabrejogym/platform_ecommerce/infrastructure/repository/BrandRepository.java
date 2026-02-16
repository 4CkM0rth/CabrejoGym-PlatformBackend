package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    
    Optional<Brand> findBySlug(String slug);
    
    List<Brand> findByActiveTrue();
    
    boolean existsBySlug(String slug);
}
