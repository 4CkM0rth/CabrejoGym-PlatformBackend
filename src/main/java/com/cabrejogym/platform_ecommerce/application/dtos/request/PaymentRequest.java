package com.cabrejogym.platform_ecommerce.application.dtos.request;

import com.cabrejogym.platform_ecommerce.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull
        Long orderId,
        
        @NotNull
        PaymentMethod paymentMethod,
        
        @NotNull
        BigDecimal amount,
        
        // Datos de tarjeta (simulados)
        String cardNumber,
        String cardHolderName,
        String cardExpiryMonth,
        String cardExpiryYear,
        String cardCvv,
        
        // Datos adicionales
        String email,
        String documentType,
        String documentNumber
) {}
