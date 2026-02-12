package com.cabrejogym.platform_ecommerce.application.mapper.address;

import com.cabrejogym.platform_ecommerce.application.dtos.response.AddressDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Address;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface AddressMapper {
    AddressDTO toDto(Address address);
}
