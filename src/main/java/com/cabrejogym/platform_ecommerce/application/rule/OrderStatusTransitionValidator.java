package com.cabrejogym.platform_ecommerce.application.rule;

import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;

import java.util.Map;
import java.util.Set;

public final class OrderStatusTransitionValidator {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.PAID, OrderStatus.CANCELLED),
            OrderStatus.PAID, Set.of(OrderStatus.SHIPPED, OrderStatus.RETURN_REQUESTED),
            OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED, OrderStatus.RETURN_REQUESTED),
            OrderStatus.DELIVERED, Set.of(OrderStatus.RETURN_REQUESTED),
            OrderStatus.CANCELLED, Set.of(),
            OrderStatus.RETURN_REQUESTED, Set.of(OrderStatus.REFUNDED),
            OrderStatus.REFUNDED, Set.of()

    );

    private OrderStatusTransitionValidator() {
    }

    public static void validate(OrderStatus current, OrderStatus next) {
        if (current == next) return;

        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.get(current);
        if (allowed == null || !allowed.contains(next)) {
            throw new ConflictException("No se puede cambiar el estado de " + current + " a " + next);
        }
    }
}
