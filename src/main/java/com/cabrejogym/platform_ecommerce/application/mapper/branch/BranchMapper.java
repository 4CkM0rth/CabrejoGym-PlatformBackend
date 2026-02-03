package com.cabrejogym.platform_ecommerce.application.mapper.branch;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.BaseMapperConfig;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class)
public interface BranchMapper {
    BranchDTO toDto(Branch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Branch fromCreateRequest(CreateBranchRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(UpdateBranchRequest request, @MappingTarget Branch branch);
}
