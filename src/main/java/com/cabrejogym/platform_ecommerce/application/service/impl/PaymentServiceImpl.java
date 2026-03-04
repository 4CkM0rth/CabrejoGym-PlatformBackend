package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.PaymentRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.PaymentResponse;
import com.cabrejogym.platform_ecommerce.application.service.PaymentService;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import com.cabrejogym.platform_ecommerce.domain.entity.Payment;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import com.cabrejogym.platform_ecommerce.domain.enums.PaymentStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.BadRequestException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Processing payment for order: {}", request.orderId());

        // Validar orden
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        // Validar que la orden esté pendiente
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("La orden ya fue procesada");
        }

        // Validar monto
        if (request.amount().compareTo(order.getTotal()) != 0) {
            throw new BadRequestException("El monto no coincide con el total de la orden");
        }

        // SIMULACIÓN: Decidir si el pago es aprobado o rechazado
        PaymentStatus status = simulatePaymentProcessing(request);
        
        // Crear registro de pago
        Payment payment = Payment.builder()
                .order(order)
                .status(status)
                .paymentMethod(request.paymentMethod())
                .amount(request.amount())
                .transactionId(generateTransactionId())
                .authorizationCode(status == PaymentStatus.APPROVED ? generateAuthCode() : null)
                .paymentDetails(buildPaymentDetails(request))
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Actualizar estado de la orden
        if (status == PaymentStatus.APPROVED) {
            order.setStatus(OrderStatus.PAID);
            log.info("Payment approved for order: {}", order.getOrderNumber());
        } else {
            order.setStatus(OrderStatus.CANCELLED);
            log.warn("Payment rejected for order: {}", order.getOrderNumber());
        }
        orderRepository.save(order);

        return mapToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==================== MÉTODOS DE SIMULACIÓN ====================

    /**
     * Simula el procesamiento del pago
     * Reglas de simulación:
     * - Tarjetas que terminan en número par: APROBADO
     * - Tarjetas que terminan en número impar: RECHAZADO
     * - Si no hay tarjeta, 80% aprobado, 20% rechazado
     */
    private PaymentStatus simulatePaymentProcessing(PaymentRequest request) {
        // Simular delay de procesamiento (100-500ms)
        try {
            Thread.sleep(100 + random.nextInt(400));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Lógica de simulación
        if (request.cardNumber() != null && !request.cardNumber().isEmpty()) {
            // Obtener último dígito de la tarjeta
            char lastDigit = request.cardNumber().charAt(request.cardNumber().length() - 1);
            int digit = Character.getNumericValue(lastDigit);
            
            // Par = Aprobado, Impar = Rechazado
            return (digit % 2 == 0) ? PaymentStatus.APPROVED : PaymentStatus.REJECTED;
        }

        // Sin tarjeta: 80% aprobado
        return (random.nextInt(100) < 80) ? PaymentStatus.APPROVED : PaymentStatus.REJECTED;
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateAuthCode() {
        return "AUTH-" + (100000 + random.nextInt(900000));
    }

    private String buildPaymentDetails(PaymentRequest request) {
        StringBuilder details = new StringBuilder();
        details.append("Payment Method: ").append(request.paymentMethod()).append("\n");
        
        if (request.cardNumber() != null) {
            String maskedCard = maskCardNumber(request.cardNumber());
            details.append("Card: ").append(maskedCard).append("\n");
        }
        
        if (request.email() != null) {
            details.append("Email: ").append(request.email()).append("\n");
        }
        
        if (request.documentNumber() != null) {
            details.append("Document: ").append(request.documentType())
                   .append(" ").append(request.documentNumber()).append("\n");
        }
        
        return details.toString();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }

    private PaymentResponse mapToResponse(Payment payment) {
        String message = switch (payment.getStatus()) {
            case APPROVED -> "Pago aprobado exitosamente";
            case REJECTED -> "Pago rechazado. Por favor, intenta con otro método de pago";
            case PENDING -> "Pago pendiente de confirmación";
            case CANCELLED -> "Pago cancelado";
            case REFUNDED -> "Pago reembolsado";
        };

        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getOrderNumber(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getTransactionId(),
                payment.getAuthorizationCode(),
                message,
                payment.getCreatedAt()
        );
    }
}
