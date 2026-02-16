package com.cabrejogym.platform_ecommerce.application.mapper.product;

import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.application.mapper.brand.BrandMapper;
import com.cabrejogym.platform_ecommerce.application.mapper.category.CategoryMapper;
import com.cabrejogym.platform_ecommerce.application.mapper.tag.TagMapper;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.ProductTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = BaseMapperConfig.class, uses = {CategoryMapper.class, BrandMapper.class, ProductVariantMapper.class, ProductImageMapper.class, TagMapper.class})
public interface ProductMapper {

    @Mapping(target = "tags", expression = "java(mapTags(product))")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOList(List<Product> products);

    default List<com.cabrejogym.platform_ecommerce.application.dtos.response.TagDTO> mapTags(Product product) {
        if (product.getTags() == null) {
            return List.of();
        }
        return product.getTags().stream()
                .map(ProductTag::getTag)
                .map(tag -> new com.cabrejogym.platform_ecommerce.application.dtos.response.TagDTO(
                        tag.getId(),
                        tag.getName(),
                        tag.getSlug()
                ))
                .collect(Collectors.toList());
    }
}

