package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.dtos.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.dtos.OrderDTO;
import com.cabrejogym.platform_ecommerce.model.OrderStatus;
import com.cabrejogym.platform_ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO create(@Valid @RequestBody CreateOrderRequest request, Authentication auth) {
        return orderService.createMyOrder(auth.getName(), request);
    }

    @GetMapping("/me")
    public List<OrderDTO> myOrders(Authentication auth) {
        return orderService.listMyOrders(auth.getName());
    }

    @GetMapping("/me/{id}")
    public OrderDTO myOrderById(@PathVariable Long id, Authentication auth) {
        return orderService.getMyOrderById(auth.getName(), id);
    }

    // ADMIN
    @GetMapping
    public List<OrderDTO> all() {
        return orderService.listAll();
    }

    @PatchMapping("/{id}/status")
    public OrderDTO changeStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}
