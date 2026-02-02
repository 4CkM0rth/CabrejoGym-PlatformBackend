package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBranchRequest;

import java.util.List;

public interface BranchService {
    // Público
    List<BranchDTO> listActive();
    BranchDTO getById(Long id);

    // Admin
    List<BranchDTO> listAll();
    BranchDTO create(CreateBranchRequest request);
    BranchDTO update(Long id, UpdateBranchRequest request);
    BranchDTO activate(Long id);
    BranchDTO deactivate(Long id);
}
