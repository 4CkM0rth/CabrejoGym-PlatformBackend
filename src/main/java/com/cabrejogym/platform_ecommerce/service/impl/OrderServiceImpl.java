package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.CreateOrderItemRequest;
import com.cabrejogym.platform_ecommerce.dtos.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.dtos.OrderDTO;
import com.cabrejogym.platform_ecommerce.dtos.OrderItemDTO;
import com.cabrejogym.platform_ecommerce.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.model.*;
import com.cabrejogym.platform_ecommerce.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.repository.UserRepository;
import com.cabrejogym.platform_ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDTO createMyOrder(String email, CreateOrderRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemReq : request.items()) {

            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + itemReq.productId()));

            // Nota: tu Producto no tiene precio todavía. Para producción deberías agregarlo.
            // Mientras tanto, simulo unitPrice = 0 para que compile.
            BigDecimal unitPrice = BigDecimal.ZERO;

            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.quantity()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);

            order.getItems().add(item);
            total = total.add(lineTotal);
        }

        order.setTotal(total);

        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> listMyOrders(String email) {
        return orderRepository.findByUser_EmailOrderByCreatedAtDesc(email)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getMyOrderById(String email, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order no encontrado con id: " + orderId));

        if (!order.getUser().getEmail().equals(email)) {
            // Puedes crear una ForbiddenException si quieres. Por ahora:
            throw new ResourceNotFoundException("Order no encontrado con id: " + orderId);
        }

        return toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> listAll() {
        return orderRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public OrderDTO updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order no encontrado con id: " + orderId));
        order.setStatus(status);
        return toDto(orderRepository.save(order));
    }

    private OrderDTO toDto(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getLineTotal()
                ))
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getUser().getEmail(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items
        );
    }
}
