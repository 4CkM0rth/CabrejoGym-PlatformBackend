package com.cabrejogym.platform_ecommerce.repository;

import com.cabrejogym.platform_ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
