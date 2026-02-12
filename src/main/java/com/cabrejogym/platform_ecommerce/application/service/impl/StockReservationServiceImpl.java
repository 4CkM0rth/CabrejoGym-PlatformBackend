package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.service.StockReservationService;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.StockReservation;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockReservationServiceImpl implements StockReservationService {

    private final StockReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void reserveStock(String sessionId, Long productId, Integer quantity, int ttlMinutes) {
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Instant now = Instant.now();
        Integer reservedStock = reservationRepository.sumActiveReservationsByProductId(productId, now);
        Integer availableStock = product.getStock() - (reservedStock != null ? reservedStock : 0);

        if (availableStock < quantity) {
            throw new ConflictException("Stock insuficiente para reservar");
        }

        StockReservation reservation = new StockReservation();
        reservation.setProduct(product);
        reservation.setQuantity(quantity);
        reservation.setSessionId(sessionId);
        reservation.setExpiresAt(now.plusSeconds(ttlMinutes * 60L));
        reservation.setActive(true);

        reservationRepository.save(reservation);
        log.info("Stock reservado: {} unidades del producto {} para sesión {}", quantity, productId, sessionId);
    }

    @Override
    @Transactional
    public void releaseReservations(String sessionId) {
        int released = reservationRepository.deactivateBySessionId(sessionId);
        log.info("Liberadas {} reservas para sesión {}", released, sessionId);
    }

    @Override
    @Transactional
    public void cleanupExpiredReservations() {
        int cleaned = reservationRepository.deactivateExpiredReservations(Instant.now());
        if (cleaned > 0) {
            log.info("Limpiadas {} reservas expiradas", cleaned);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAvailableStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Instant now = Instant.now();
        Integer reservedStock = reservationRepository.sumActiveReservationsByProductId(productId, now);
        
        return product.getStock() - (reservedStock != null ? reservedStock : 0);
    }
}
