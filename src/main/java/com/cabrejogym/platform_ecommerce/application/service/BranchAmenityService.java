package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchAmenityRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchAmenityDTO;

import java.util.List;

public interface BranchAmenityService {
    BranchAmenityDTO addAmenity(Long branchId, CreateBranchAmenityRequest request);
    List<BranchAmenityDTO> getAmenitiesByBranchId(Long branchId);
    BranchAmenityDTO updateAmenity(Long amenityId, CreateBranchAmenityRequest request);
    void deleteAmenity(Long amenityId);
}
