package com.cabrejogym.platform_ecommerce.application.mapper.branch;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, uses = {BranchImageMapper.class, BranchAmenityMapper.class, MembershipPlanMapper.class})
public interface BranchMapper {
    
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "totalReviews", ignore = true)
    BranchDTO toDto(Branch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "membershipPlans", ignore = true)
    Branch fromCreateRequest(CreateBranchRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "membershipPlans", ignore = true)
    void updateEntity(UpdateBranchRequest request, @MappingTarget Branch branch);
}
