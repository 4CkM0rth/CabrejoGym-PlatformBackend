package com.cabrejogym.platform_ecommerce.application.mapper.coupon;

import com.cabrejogym.platform_ecommerce.application.dtos.response.CouponDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Coupon;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface CouponMapper {
    CouponDTO toDto(Coupon coupon);
}
