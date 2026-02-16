package com.cabrejogym.platform_ecommerce.application.mapper.tag;

import com.cabrejogym.platform_ecommerce.application.dtos.response.TagDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface TagMapper {

    TagDTO toDTO(Tag tag);

    List<TagDTO> toDTOList(List<Tag> tags);
}
