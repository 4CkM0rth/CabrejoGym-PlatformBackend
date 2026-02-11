package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderDTO createMyOrder(String email, CreateOrderRequest request);

    List<OrderDTO> listMyOrders(String email);

    OrderDTO getMyOrderById(String email, Long orderId);

    OrderDTO updateStatus(Long orderId, OrderStatus status);

    OrderDTO cancelMyOrder(String email, Long orderId);

    OrderDTO cancelAnyOrder(Long orderId);

    Page<OrderDTO> listAll(int page, int size);

    OrderDTO getAnyOrderById(Long orderId);
}
