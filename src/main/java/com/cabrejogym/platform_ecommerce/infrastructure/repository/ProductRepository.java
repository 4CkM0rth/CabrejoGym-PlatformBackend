package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
    
    Optional<Product> findBySlug(String slug);
    
    Page<Product> findByStatus(PublicationStatus status, Pageable pageable);
    
    @Query("""
           SELECT p FROM Product p
           WHERE (:status IS NULL OR p.status = :status)
           AND (:categoryId IS NULL OR p.category.id = :categoryId)
           AND (:brandId IS NULL OR p.brand.id = :brandId)
           AND (:minPrice IS NULL OR p.price >= :minPrice)
           AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           AND (:inStock IS NULL OR (:inStock = true AND p.stock > 0) OR (:inStock = false))
           ORDER BY p.createdAt DESC
           """)
    Page<Product> findWithFilters(
            @Param("status") PublicationStatus status,
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("inStock") Boolean inStock,
            Pageable pageable
    );
    
    @Query("""
           SELECT p FROM Product p
           WHERE p.status = :status
           AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :search, '%')))
           ORDER BY p.createdAt DESC
           """)
    Page<Product> searchByText(
            @Param("search") String search,
            @Param("status") PublicationStatus status,
            Pageable pageable
    );
    
    @Query("""
           SELECT p FROM Product p
           WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :query, '%')))
           ORDER BY p.createdAt DESC
           """)
    Page<Product> searchByNameOrDescription(@Param("query") String query, Pageable pageable);
    
    @Query("""
           SELECT p FROM Product p
           WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
           AND (:brandId IS NULL OR p.brand.id = :brandId)
           AND (:minPrice IS NULL OR p.price >= :minPrice)
           AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           AND (:status IS NULL OR p.status = :status)
           AND (:inStock IS NULL OR (:inStock = true AND p.stock > 0) OR (:inStock = false))
           ORDER BY p.createdAt DESC
           """)
    Page<Product> filterProducts(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") PublicationStatus status,
            @Param("inStock") Boolean inStock,
            Pageable pageable
    );
    
    boolean existsBySlug(String slug);
}
