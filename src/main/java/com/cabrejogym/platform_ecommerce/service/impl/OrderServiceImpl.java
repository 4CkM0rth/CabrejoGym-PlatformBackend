package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.CreateOrderItemRequest;
import com.cabrejogym.platform_ecommerce.dtos.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.dtos.OrderDTO;
import com.cabrejogym.platform_ecommerce.dtos.OrderItemDTO;
import com.cabrejogym.platform_ecommerce.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.exceptions.ForbiddenException;
import com.cabrejogym.platform_ecommerce.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.model.*;
import com.cabrejogym.platform_ecommerce.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.repository.UserRepository;
import com.cabrejogym.platform_ecommerce.service.OrderService;
import com.cabrejogym.platform_ecommerce.service.validation.OrderStatusTransitionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final int MONEY_SCALE = 2;

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

            Product product = productRepository.findByIdForUpdate(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + itemReq.productId()));

            if (product.getStock() == null) {
                throw new ConflictException("El producto no tiene stock configurado: " + product.getName());
            }

            if (itemReq.quantity() == null || itemReq.quantity() <= 0) {
                throw new ConflictException("La cantidad debe ser mayor a 0");
            }

            if (product.getStock() < itemReq.quantity()) {
                throw new ConflictException("Stock insuficiente para el producto: " + product.getName());
            }

            BigDecimal unitPrice = product.getPrice();
            if (unitPrice == null) {
                throw new ConflictException("El producto no tiene precio configurado: " + product.getName());
            }

            if (Boolean.TRUE.equals(product.getHasDiscount())
                    && product.getDiscountPercent() != null
                    && product.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal discount = unitPrice
                        .multiply(product.getDiscountPercent())
                        .divide(BigDecimal.valueOf(100), MONEY_SCALE, RoundingMode.HALF_UP);

                unitPrice = unitPrice.subtract(discount);
            }

            unitPrice = unitPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);

            BigDecimal lineTotal = unitPrice
                    .multiply(BigDecimal.valueOf(itemReq.quantity()))
                    .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);

            order.getItems().add(item);
            total = total.add(lineTotal);

            product.setStock(product.getStock() - itemReq.quantity());

        }

        order.setTotal(total.setScale(MONEY_SCALE, RoundingMode.HALF_UP));

        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> listMyOrders(String email) {
        return orderRepository.findByUser_EmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getMyOrderById(String email, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        if (!order.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("No tienes permiso para ver esta orden");
        }

        return toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> listAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDTO updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order no encontrado con id: " + orderId));

        OrderStatusTransitionValidator.validate(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        return toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO cancelMyOrder(String email, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        if (!order.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("No tienes permiso para cancelar esta orden");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ConflictException("La orden ya está cancelada");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            if (order.getStatus() == OrderStatus.PAID) {
                throw new ConflictException("No se puede cancelar una orden pagada. Debes iniciar una devolución.");
            }
            throw new ConflictException("No se puede cancelar la orden en estado: " + order.getStatus());
        }

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findByIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con id: " + item.getProduct().getId()
                    ));

            product.setStock(product.getStock() + item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    @Transactional
    public OrderDTO cancelAnyOrder(Long orderId) {

        Order order = orderRepository.findByIdWithItemsForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        return cancelAndRestock(order);
    }

    private OrderDTO cancelAndRestock(Order order) {

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ConflictException("La orden ya está cancelada");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new ConflictException("No se puede cancelar una orden en estado: " + order.getStatus());
        }

        for (OrderItem item : order.getItems()) {

            Long productId = item.getProduct().getId();

            Product product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

            product.setStock(product.getStock() + item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order saved = orderRepository.save(order);
        return toDto(saved);
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