package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findBySlug(String slug);
    
    List<Category> findByParentIsNull();

    List<Category> findByActiveTrue();

    List<Category> findByParentId(Long parentId);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.displayOrder, c.name")
    List<Category> findRootCategories();
    
    boolean existsBySlug(String slug);
}
