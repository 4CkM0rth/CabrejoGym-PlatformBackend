package com.cabrejogym.platform_ecommerce.application.mapper.branch;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchReviewDTO;
import com.cabrejogym.platform_ecommerce.domain.entity.BranchReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchReviewMapper {
    
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userName")
    BranchReviewDTO toDto(BranchReview entity);
}
