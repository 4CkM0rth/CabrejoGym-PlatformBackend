package com.cabrejogym.platform_ecommerce.application.mapper.order;

import com.cabrejogym.platform_ecommerce.application.dtos.response.OrderDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.application.mapper.address.AddressMapper;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class, uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "couponCode", source = "coupon.code")
    OrderDTO toDto(Order order);
}
