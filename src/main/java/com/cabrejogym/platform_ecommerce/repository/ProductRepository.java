package com.cabrejogym.platform_ecommerce.repository;

import com.cabrejogym.platform_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
