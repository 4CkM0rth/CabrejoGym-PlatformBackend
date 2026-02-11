package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderItemRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.application.mapper.order.OrderMapper;
import com.cabrejogym.platform_ecommerce.application.service.RefundService;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private RefundService refundService;
    @Mock private OrderMapper orderMapper;

    @InjectMocks private OrderServiceImpl orderService;

    @Test
    void createOrder_success_stockReduced() {
        User user = new User();
        user.setEmail("test@mail.com");

        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(BigDecimal.valueOf(100));
        product.setHasDiscount(false);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));

        CreateOrderItemRequest item = new CreateOrderItemRequest(1L, 2);
        CreateOrderRequest request = new CreateOrderRequest(List.of(item));

        orderService.createMyOrder("test@mail.com", request);

        assertEquals(8, product.getStock());
    }

    @Test
    void createOrder_stockInsufficient_throws() {
        User user = new User();
        user.setEmail("test@mail.com");

        Product product = new Product();
        product.setId(1L);
        product.setStock(1);
        product.setPrice(BigDecimal.valueOf(100));
        product.setHasDiscount(false);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));

        CreateOrderItemRequest item = new CreateOrderItemRequest(1L, 2);
        CreateOrderRequest request = new CreateOrderRequest(List.of(item));

        assertThrows(ConflictException.class, () ->
                orderService.createMyOrder("test@mail.com", request)
        );
    }
}