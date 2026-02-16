package com.cabrejogym.platform_ecommerce.application.mapper.product;

import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductImageDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface ProductImageMapper {

    ProductImageDTO toDTO(ProductImage image);

    List<ProductImageDTO> toDTOList(List<ProductImage> images);
}
