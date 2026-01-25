package com.cabrejogym.platform_ecommerce.repository;

import com.cabrejogym.platform_ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_EmailOrderByCreatedAtDesc(String email);
}
