package com.cabrejogym.platform_ecommerce.application.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import com.cabrejogym.platform_ecommerce.application.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public OrderDTO getAnyById(@PathVariable Long id) {
        return orderService.getAnyOrderById(id);
    }

    @GetMapping("/me/{id}")
    public OrderDTO myOrderById(@PathVariable Long id, Authentication auth) {
        return orderService.getMyOrderById(auth.getName(), id);
    }

    @PatchMapping("/me/{id}/cancel")
    public OrderDTO cancelMyOrder(@PathVariable Long id, Authentication auth) {
        return orderService.cancelMyOrder(auth.getName(), id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<OrderDTO> all(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size) {
        return orderService.listAll(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public OrderDTO changeStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cancel")
    public OrderDTO cancelAnyOrder(@PathVariable Long id) {
        return orderService.cancelAnyOrder(id);
    }

}
