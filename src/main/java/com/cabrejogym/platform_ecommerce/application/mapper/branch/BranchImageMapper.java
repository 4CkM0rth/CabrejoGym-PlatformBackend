package com.cabrejogym.platform_ecommerce.application.mapper.branch;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchImageDTO;
import com.cabrejogym.platform_ecommerce.domain.entity.BranchImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchImageMapper {
    
    @Mapping(source = "branch.id", target = "branchId")
    BranchImageDTO toDto(BranchImage entity);
}
