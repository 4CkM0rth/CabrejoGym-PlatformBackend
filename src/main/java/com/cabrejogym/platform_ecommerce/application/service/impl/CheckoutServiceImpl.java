package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CheckoutRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderTotalsDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.order.OrderMapper;
import com.cabrejogym.platform_ecommerce.application.service.CheckoutService;
import com.cabrejogym.platform_ecommerce.domain.entity.*;
import com.cabrejogym.platform_ecommerce.domain.enums.CouponType;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private static final int MONEY_SCALE = 2;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax
    private static final BigDecimal SHIPPING_COST = new BigDecimal("5.00");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("50.00");

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    
    private final AtomicLong orderCounter = new AtomicLong(System.currentTimeMillis() % 100000);

    @Override
    @Transactional(readOnly = true)
    public OrderTotalsDTO calculateTotals(String email, String sessionId, String couponCode) {
        Cart cart = findCart(email, sessionId);
        
        if (cart.getItems().isEmpty()) {
            throw new ConflictException("El carrito está vacío");
        }

        BigDecimal subtotal = calculateSubtotal(cart);
        BigDecimal discount = BigDecimal.ZERO;
        
        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = validateAndGetCoupon(couponCode, subtotal);
            discount = calculateDiscount(coupon, subtotal);
        }

        BigDecimal afterDiscount = subtotal.subtract(discount);
        BigDecimal tax = afterDiscount.multiply(TAX_RATE).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal shipping = afterDiscount.compareTo(FREE_SHIPPING_THRESHOLD) >= 0 
                ? BigDecimal.ZERO 
                : SHIPPING_COST;
        BigDecimal total = afterDiscount.add(tax).add(shipping);

        return new OrderTotalsDTO(subtotal, discount, tax, shipping, total);
    }

    @Override
    @Transactional
    public OrderDTO checkout(String email, String sessionId, CheckoutRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Cart cart = findCart(email, sessionId);
        
        if (cart.getItems().isEmpty()) {
            throw new ConflictException("El carrito está vacío");
        }

        Address shippingAddress = addressRepository.findByIdAndUser_Email(request.shippingAddressId(), email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección de envío no encontrada"));

        Address billingAddress = addressRepository.findByIdAndUser_Email(request.billingAddressId(), email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección de facturación no encontrada"));

        BigDecimal subtotal = calculateSubtotal(cart);
        BigDecimal discount = BigDecimal.ZERO;
        Coupon coupon = null;

        if (request.couponCode() != null && !request.couponCode().isBlank()) {
            coupon = validateAndGetCoupon(request.couponCode(), subtotal);
            discount = calculateDiscount(coupon, subtotal);
            
            coupon.setUsageCount(coupon.getUsageCount() + 1);
            couponRepository.save(coupon);
        }

        BigDecimal afterDiscount = subtotal.subtract(discount);
        BigDecimal tax = afterDiscount.multiply(TAX_RATE).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal shipping = afterDiscount.compareTo(FREE_SHIPPING_THRESHOLD) >= 0 
                ? BigDecimal.ZERO 
                : SHIPPING_COST;
        BigDecimal total = afterDiscount.add(tax).add(shipping);

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setSubtotal(subtotal);
        order.setDiscount(discount);
        order.setTax(tax);
        order.setShipping(shipping);
        order.setTotal(total);
        order.setCoupon(coupon);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);

        for (CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findByIdForUpdate(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new ConflictException("Stock insuficiente para: " + product.getName());
            }

            BigDecimal unitPrice = calculateUnitPrice(product);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                    .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setLineTotal(lineTotal);

            order.getItems().add(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());
        }

        Order saved = orderRepository.save(order);
        
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(saved);
    }

    private Cart findCart(String email, String sessionId) {
        if (email != null) {
            return cartRepository.findByUserEmailWithItems(email)
                    .orElseThrow(() -> new ConflictException("Carrito no encontrado"));
        } else if (sessionId != null) {
            return cartRepository.findBySessionIdWithItems(sessionId)
                    .orElseThrow(() -> new ConflictException("Carrito no encontrado"));
        }
        throw new ConflictException("Se requiere email o sessionId");
    }

    private BigDecimal calculateSubtotal(Cart cart) {
        return cart.getItems().stream()
                .map(item -> {
                    BigDecimal unitPrice = calculateUnitPrice(item.getProduct());
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateUnitPrice(Product product) {
        BigDecimal price = product.getPrice();
        
        if (Boolean.TRUE.equals(product.getHasDiscount()) && product.getDiscountPercent() != null) {
            BigDecimal discountAmount = price.multiply(product.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), MONEY_SCALE, RoundingMode.HALF_UP);
            price = price.subtract(discountAmount);
        }
        
        return price.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private Coupon validateAndGetCoupon(String code, BigDecimal subtotal) {
        Coupon coupon = couponRepository.findByCodeForUpdate(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));

        Instant now = Instant.now();
        
        if (!coupon.getActive()) {
            throw new ConflictException("El cupón no está activo");
        }
        
        if (now.isBefore(coupon.getValidFrom())) {
            throw new ConflictException("El cupón aún no es válido");
        }
        
        if (now.isAfter(coupon.getValidUntil())) {
            throw new ConflictException("El cupón ha expirado");
        }
        
        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new ConflictException("El cupón ha alcanzado su límite de uso");
        }

        if (coupon.getMinPurchase() != null && subtotal.compareTo(coupon.getMinPurchase()) < 0) {
            throw new ConflictException("El monto mínimo de compra es: " + coupon.getMinPurchase());
        }

        return coupon;
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal subtotal) {
        BigDecimal discount;
        
        if (coupon.getType() == CouponType.PERCENTAGE) {
            discount = subtotal.multiply(coupon.getValue())
                    .divide(BigDecimal.valueOf(100), MONEY_SCALE, RoundingMode.HALF_UP);
        } else {
            discount = coupon.getValue();
        }

        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }

        return discount.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long sequence = orderCounter.incrementAndGet();
        return String.format("ORD-%s-%05d", date, sequence % 100000);
    }
}
