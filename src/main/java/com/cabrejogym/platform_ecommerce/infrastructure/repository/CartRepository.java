package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUser_Email(String email);
    
    Optional<Cart> findBySessionId(String sessionId);
    
    @Query("""
           SELECT c
           FROM Cart c
           LEFT JOIN FETCH c.items i
           LEFT JOIN FETCH i.product
           WHERE c.user.email = :email
           """)
    Optional<Cart> findByUserEmailWithItems(@Param("email") String email);
    
    @Query("""
           SELECT c
           FROM Cart c
           LEFT JOIN FETCH c.items i
           LEFT JOIN FETCH i.product
           WHERE c.sessionId = :sessionId
           """)
    Optional<Cart> findBySessionIdWithItems(@Param("sessionId") String sessionId);
}
