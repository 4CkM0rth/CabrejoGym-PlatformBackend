package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.PaymentRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByTransactionId(String transactionId);
    List<PaymentResponse> getPaymentsByOrderId(Long orderId);
}
