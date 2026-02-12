package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CheckoutRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderTotalsDTO;

public interface CheckoutService {
    
    OrderTotalsDTO calculateTotals(String email, String sessionId, String couponCode);
    
    OrderDTO checkout(String email, String sessionId, CheckoutRequest request);
}
