package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CheckoutRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderTotalsDTO;
import com.cabrejogym.platform_ecommerce.application.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/totals")
    public OrderTotalsDTO calculateTotals(@RequestParam(required = false) String couponCode,
                                          Authentication auth,
                                          HttpSession session) {
        String email = auth != null ? auth.getName() : null;
        String sessionId = session.getId();
        return checkoutService.calculateTotals(email, sessionId, couponCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO checkout(@Valid @RequestBody CheckoutRequest request,
                            Authentication auth,
                            HttpSession session) {
        if (auth == null) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        return checkoutService.checkout(auth.getName(), session.getId(), request);
    }
}
