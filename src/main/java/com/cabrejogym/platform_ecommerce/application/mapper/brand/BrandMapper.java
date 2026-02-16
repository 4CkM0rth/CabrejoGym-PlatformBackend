package com.cabrejogym.platform_ecommerce.application.mapper.brand;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BrandDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Brand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface BrandMapper {

    BrandDTO toDTO(Brand brand);

    List<BrandDTO> toDTOList(List<Brand> brands);
}
