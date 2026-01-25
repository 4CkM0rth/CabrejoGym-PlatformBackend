package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.dtos.OrderDTO;
import com.cabrejogym.platform_ecommerce.model.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO createMyOrder(String email, CreateOrderRequest request);
    List<OrderDTO> listMyOrders(String email);
    OrderDTO getMyOrderById(String email, Long orderId);

    // ADMIN
    List<OrderDTO> listAll();
    OrderDTO updateStatus(Long orderId, OrderStatus status);
}
