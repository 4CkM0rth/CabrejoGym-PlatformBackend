package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderItemRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateOrderRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateRefundRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.order.OrderMapper;
import com.cabrejogym.platform_ecommerce.application.rule.OrderStatusTransitionValidator;
import com.cabrejogym.platform_ecommerce.application.service.OrderService;
import com.cabrejogym.platform_ecommerce.application.service.RefundService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final int MONEY_SCALE = 2;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

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

        validateNoDuplicateProducts(request);

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateSimpleOrderNumber());
        applyStatus(order, OrderStatus.PENDING);

        BigDecimal subtotal = BigDecimal.ZERO;

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
            subtotal = subtotal.add(lineTotal);

            product.setStock(product.getStock() - itemReq.quantity());
        }

        order.setSubtotal(subtotal.setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        order.setDiscount(BigDecimal.ZERO);
        order.setTax(BigDecimal.ZERO);
        order.setShipping(BigDecimal.ZERO);
        order.setTotal(subtotal.setScale(MONEY_SCALE, RoundingMode.HALF_UP));

        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    private String generateSimpleOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getAnyOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> listAll(int page, int size) {

        int safePage = Math.max(page, 0);

        int normalizedSize = (size <= 0) ? DEFAULT_PAGE_SIZE : size;
        int safeSize = Math.min(normalizedSize, MAX_PAGE_SIZE);

        PageRequest pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return orderRepository.findAll(pageable).map(orderMapper::toDto);
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
    @Transactional
    public OrderDTO updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findByIdWithItemsForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        applyStatus(order, newStatus);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO cancelMyOrder(String email, Long orderId) {

        Order order = orderRepository.findByIdWithItemsForUpdate(orderId)
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

                applyStatus(order, OrderStatus.RETURN_REQUESTED);
                return orderMapper.toDto(orderRepository.save(order));
            }

            throw new ConflictException("No se puede cancelar la orden en estado: " + order.getStatus());
        }

        restock(order);
        applyStatus(order, OrderStatus.CANCELLED);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO cancelAnyOrder(Long orderId) {

        Order order = orderRepository.findByIdWithItemsForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + orderId));

        if (order.getStatus() == OrderStatus.PAID) {

            refundService.createMyRefund(
                    order.getUser().getEmail(),
                    new CreateRefundRequest(order.getId(), "Cancelación admin (orden pagada)")
            );

            applyStatus(order, OrderStatus.RETURN_REQUESTED);
            return orderMapper.toDto(orderRepository.save(order));
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new ConflictException("La orden ya está cancelada");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new ConflictException("No se puede cancelar una orden en estado: " + order.getStatus());
        }

        restock(order);
        applyStatus(order, OrderStatus.CANCELLED);

        return orderMapper.toDto(orderRepository.save(order));
    }

    private void applyStatus(Order order, OrderStatus next) {
        OrderStatus current = order.getStatus();
        if (current == null) {
            order.setStatus(next);
            return;
        }
        OrderStatusTransitionValidator.validate(current, next);
        order.setStatus(next);
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
        BigDecimal percent = product.getDiscountPercent();

        if (Boolean.TRUE.equals(product.getHasDiscount())
                && percent != null
                && percent.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal discount = unitPrice
                    .multiply(percent)
                    .divide(BigDecimal.valueOf(100), MONEY_SCALE, RoundingMode.HALF_UP);

            unitPrice = unitPrice.subtract(discount);
        }

        return unitPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private void validateNoDuplicateProducts(CreateOrderRequest request) {
        long distinct = request.items().stream()
                .map(CreateOrderItemRequest::productId)
                .distinct()
                .count();

        if (distinct != request.items().size()) {
            throw new ConflictException("La orden no puede contener productos repetidos. Unifica cantidades por producto.");
        }
    }
}