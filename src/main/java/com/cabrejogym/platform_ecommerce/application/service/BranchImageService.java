package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchImageRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BranchImageService {
    BranchImageDTO addImage(Long branchId, CreateBranchImageRequest request);
    BranchImageDTO uploadImage(Long branchId, MultipartFile file, String altText, Integer displayOrder, Boolean isPrimary);
    List<BranchImageDTO> getImagesByBranchId(Long branchId);
    BranchImageDTO setPrimaryImage(Long imageId);
    void deleteImage(Long imageId);
}
