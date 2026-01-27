package com.cabrejogym.platform_ecommerce.repository;

import com.cabrejogym.platform_ecommerce.model.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_EmailOrderByCreatedAtDesc(String email);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT o
           FROM Order o
           LEFT JOIN FETCH o.items i
           LEFT JOIN FETCH i.product p
           WHERE o.id = :id
           """)
    Optional<Order> findByIdWithItemsForUpdate(@Param("id") Long id);
}