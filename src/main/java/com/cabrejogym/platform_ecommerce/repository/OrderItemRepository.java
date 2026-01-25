package com.cabrejogym.platform_ecommerce.repository;

import com.cabrejogym.platform_ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
