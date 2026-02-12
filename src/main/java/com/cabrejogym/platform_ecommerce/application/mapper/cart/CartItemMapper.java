package com.cabrejogym.platform_ecommerce.application.mapper.cart;

import com.cabrejogym.platform_ecommerce.application.dtos.response.CartItemDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public interface CartItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", expression = "java(calculatePrice(item))")
    @Mapping(target = "lineTotal", expression = "java(calculateLineTotal(item))")
    CartItemDTO toDto(CartItem item);

    default java.math.BigDecimal calculatePrice(CartItem item) {
        java.math.BigDecimal price = item.getProduct().getPrice();
        if (Boolean.TRUE.equals(item.getProduct().getHasDiscount())) {
            java.math.BigDecimal discount = price.multiply(item.getProduct().getDiscountPercent())
                    .divide(java.math.BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            price = price.subtract(discount);
        }
        return price.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    default java.math.BigDecimal calculateLineTotal(CartItem item) {
        return calculatePrice(item).multiply(java.math.BigDecimal.valueOf(item.getQuantity()))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
