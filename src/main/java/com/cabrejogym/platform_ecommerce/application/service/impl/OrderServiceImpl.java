package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderItemRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateRefundRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderItemDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.order.OrderMapper;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import com.cabrejogym.platform_ecommerce.domain.entity.OrderItem;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ForbiddenException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import com.cabrejogym.platform_ecommerce.application.service.OrderService;
import com.cabrejogym.platform_ecommerce.application.service.RefundService;
import com.cabrejogym.platform_ecommerce.application.rule.OrderStatusTransitionValidator;
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
    private final RefundService refundService;
    private final OrderMapper orderMapper;

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

            validateOrderItem(product, itemReq);

            BigDecimal unitPrice = calculateUnitPrice(product);
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
        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> listMyOrders(String email) {
        return orderRepository.findByUser_EmailOrderByCreatedAtDesc(email)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getMyOrderById(String email, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        requireOwner(order, email);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> listAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDTO updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order no encontrado con id: " + orderId));

        OrderStatusTransitionValidator.validate(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO cancelMyOrder(String email, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        requireOwner(order, email);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ConflictException("La orden ya está cancelada");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            if (order.getStatus() == OrderStatus.PAID) {
                refundService.createMyRefund(
                        email,
                        new CreateRefundRequest(order.getId(), "Cancelación solicitada por el usuario (orden pagada)")
                );
                return orderMapper.toDto(order);
            }
            throw new ConflictException("No se puede cancelar la orden en estado: " + order.getStatus());
        }

        restock(order);
        order.setStatus(OrderStatus.CANCELLED);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO cancelAnyOrder(Long orderId) {

        Order order = orderRepository.findByIdWithItemsForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        return cancelAndRestock(order);
    }

    private OrderDTO cancelAndRestock(Order order) {

        if (order.getStatus() == OrderStatus.PAID) {
            refundService.createMyRefund(
                    order.getUser().getEmail(),
                    new CreateRefundRequest(order.getId(), "Cancelación admin (orden pagada)")
            );
            return orderMapper.toDto(order);
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ConflictException("La orden ya está cancelada");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new ConflictException("No se puede cancelar una orden en estado: " + order.getStatus());
        }

        restock(order);
        order.setStatus(OrderStatus.CANCELLED);

        return orderMapper.toDto(orderRepository.save(order));
    }

    private void requireOwner(Order order, String email) {
        if (!order.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("No tienes permiso para esta orden");
        }
    }

    private void restock(Order order) {
        for (OrderItem item : order.getItems()) {
            Long productId = item.getProduct().getId();

            Product product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

            product.setStock(product.getStock() + item.getQuantity());
        }
    }

    private void validateOrderItem(Product product, CreateOrderItemRequest itemReq) {

        if (product.getStock() == null) {
            throw new ConflictException("El producto no tiene stock configurado: " + product.getName());
        }

        if (itemReq.quantity() == null || itemReq.quantity() <= 0) {
            throw new ConflictException("La cantidad debe ser mayor a 0");
        }

        if (product.getStock() < itemReq.quantity()) {
            throw new ConflictException("Stock insuficiente para el producto: " + product.getName());
        }

        if (product.getPrice() == null) {
            throw new ConflictException("El producto no tiene precio configurado: " + product.getName());
        }
    }

    private BigDecimal calculateUnitPrice(Product product) {

        BigDecimal unitPrice = product.getPrice();

        if (Boolean.TRUE.equals(product.getHasDiscount())
                && product.getDiscountPercent() != null
                && product.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal discount = unitPrice
                    .multiply(product.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), MONEY_SCALE, RoundingMode.HALF_UP);

            unitPrice = unitPrice.subtract(discount);
        }

        return unitPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}