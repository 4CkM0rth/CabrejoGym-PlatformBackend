package com.cabrejogym.platform_ecommerce.application.service;

public interface StockReservationService {
    
    void reserveStock(String sessionId, Long productId, Integer quantity, int ttlMinutes);
    
    void releaseReservations(String sessionId);
    
    void cleanupExpiredReservations();
    
    Integer getAvailableStock(Long productId);
}
