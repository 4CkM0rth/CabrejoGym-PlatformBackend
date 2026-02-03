package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBranchRequest;

import java.util.List;

public interface BranchService {
    List<BranchDTO> listActive();
    BranchDTO getActiveById(Long id);

    List<BranchDTO> listAll();
    BranchDTO getById(Long id);
    BranchDTO create(CreateBranchRequest request);
    BranchDTO update(Long id, UpdateBranchRequest request);
    BranchDTO activate(Long id);
    BranchDTO deactivate(Long id);
}
