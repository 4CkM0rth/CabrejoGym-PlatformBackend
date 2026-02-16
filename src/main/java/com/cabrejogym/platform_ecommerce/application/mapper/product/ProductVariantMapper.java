package com.cabrejogym.platform_ecommerce.application.mapper.product;

import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductVariantDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.ProductVariant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface ProductVariantMapper {

    ProductVariantDTO toDTO(ProductVariant variant);

    List<ProductVariantDTO> toDTOList(List<ProductVariant> variants);
}
