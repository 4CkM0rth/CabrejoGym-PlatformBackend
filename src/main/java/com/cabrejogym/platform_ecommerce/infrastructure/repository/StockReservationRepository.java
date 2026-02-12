package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {
    
    List<StockReservation> findBySessionIdAndActiveTrue(String sessionId);
    
    @Query("""
           SELECT COALESCE(SUM(sr.quantity), 0)
           FROM StockReservation sr
           WHERE sr.product.id = :productId
           AND sr.active = true
           AND sr.expiresAt > :now
           """)
    Integer sumActiveReservationsByProductId(@Param("productId") Long productId, @Param("now") Instant now);
    
    @Modifying
    @Query("""
           UPDATE StockReservation sr
           SET sr.active = false
           WHERE sr.expiresAt <= :now
           AND sr.active = true
           """)
    int deactivateExpiredReservations(@Param("now") Instant now);
    
    @Modifying
    @Query("""
           UPDATE StockReservation sr
           SET sr.active = false
           WHERE sr.sessionId = :sessionId
           AND sr.active = true
           """)
    int deactivateBySessionId(@Param("sessionId") String sessionId);
}
