package com.cabrejogym.platform_ecommerce.application.mapper.branch;

import com.cabrejogym.platform_ecommerce.application.dtos.response.MembershipPlanDTO;
import com.cabrejogym.platform_ecommerce.domain.entity.MembershipPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembershipPlanMapper {
    
    @Mapping(source = "branch.id", target = "branchId")
    MembershipPlanDTO toDto(MembershipPlan entity);
}
