package com.cabrejogym.platform_ecommerce.infrastructure.scheduler;

import com.cabrejogym.platform_ecommerce.application.service.StockReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockReservationCleanupScheduler {

    private final StockReservationService stockReservationService;

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredReservations() {
        try {
            stockReservationService.cleanupExpiredReservations();
        } catch (Exception e) {
            log.error("Error al limpiar reservas expiradas", e);
        }
    }
}
