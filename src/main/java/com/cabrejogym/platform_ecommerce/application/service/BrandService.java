package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBrandRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBrandRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BrandDTO;

import java.util.List;

public interface BrandService {
    BrandDTO createBrand(CreateBrandRequest request);
    BrandDTO updateBrand(Long id, UpdateBrandRequest request);
    void deleteBrand(Long id);
    BrandDTO getBrandById(Long id);
    BrandDTO getBrandBySlug(String slug);
    List<BrandDTO> getAllBrands();
    List<BrandDTO> getActiveBrands();
}
