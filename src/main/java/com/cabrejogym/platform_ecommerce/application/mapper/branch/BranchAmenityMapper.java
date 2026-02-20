package com.cabrejogym.platform_ecommerce.application.mapper.branch;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchAmenityDTO;
import com.cabrejogym.platform_ecommerce.domain.entity.BranchAmenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchAmenityMapper {
    
    @Mapping(source = "branch.id", target = "branchId")
    BranchAmenityDTO toDto(BranchAmenity entity);
}
