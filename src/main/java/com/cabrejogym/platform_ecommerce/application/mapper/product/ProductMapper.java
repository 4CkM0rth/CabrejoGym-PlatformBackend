package com.cabrejogym.platform_ecommerce.application.mapper.product;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "hasDiscount", source = "hasDiscount"),
            @Mapping(target = "discountPercent", source = "discountPercent"),
            @Mapping(target = "stock", source = "stock")
    })
    ProductDTO toDto(Product product);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "hasDiscount", source = "hasDiscount"),
            @Mapping(target = "discountPercent", source = "discountPercent"),
            @Mapping(target = "stock", source = "stock")
    })
    Product toEntity(CreateProductRequest request);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "hasDiscount", source = "hasDiscount"),
            @Mapping(target = "discountPercent", source = "discountPercent"),
            @Mapping(target = "stock", source = "stock")
    })
    void updateEntity(@MappingTarget Product product, UpdateProductRequest request);
}
