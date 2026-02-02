package com.cabrejogym.platform_ecommerce.application.mapper.refund;

import com.cabrejogym.platform_ecommerce.application.dtos.response.RefundDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public interface RefundMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "userEmail", source = "user.email")
    RefundDTO toDto(Refund refund);
}
