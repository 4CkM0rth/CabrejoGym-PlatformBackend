package com.cabrejogym.platform_ecommerce.application.mapper.category;

import com.cabrejogym.platform_ecommerce.application.dtos.response.CategoryDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    CategoryDTO toDTO(Category category);

    List<CategoryDTO> toDTOList(List<Category> categories);
}
