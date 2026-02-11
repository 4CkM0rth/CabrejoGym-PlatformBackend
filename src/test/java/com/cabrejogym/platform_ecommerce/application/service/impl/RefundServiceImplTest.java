package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AdminRefundDecisionRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateRefundRequest;
import com.cabrejogym.platform_ecommerce.application.mapper.refund.RefundMapper;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import com.cabrejogym.platform_ecommerce.domain.entity.Refund;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import com.cabrejogym.platform_ecommerce.domain.enums.RefundStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.RefundRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefundServiceImplTest {

    @Mock private RefundRepository refundRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private RefundMapper refundMapper;

    @InjectMocks private RefundServiceImpl refundService;

    @Test
    void createRefund_setsOrderToReturnRequested() {
        User user = new User();
        user.setEmail("u@mail.com");

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PAID);
        order.setTotal(BigDecimal.valueOf(100));

        when(userRepository.findByEmail("u@mail.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(refundRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        refundService.createMyRefund("u@mail.com", new CreateRefundRequest(1L, "motivo"));

        assertEquals(OrderStatus.RETURN_REQUESTED, order.getStatus());
    }

    @Test
    void markAsRefunded_changesRefundAndOrderStatus() {
        Order order = new Order();
        order.setStatus(OrderStatus.RETURN_REQUESTED);

        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setStatus(RefundStatus.APPROVED);

        when(refundRepository.findByIdWithOrderForUpdate(1L)).thenReturn(Optional.of(refund));
        when(refundRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        refundService.markAsRefunded(1L, new AdminRefundDecisionRequest("ok"));

        assertEquals(RefundStatus.REFUNDED, refund.getStatus());
        assertEquals(OrderStatus.REFUNDED, order.getStatus());
    }
}