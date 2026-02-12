package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateCouponRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CouponDTO;
import org.springframework.data.domain.Page;

public interface CouponService {
    
    CouponDTO create(CreateCouponRequest request);
    
    CouponDTO getByCode(String code);
    
    CouponDTO validateCoupon(String code);
    
    Page<CouponDTO> listAll(int page, int size);
    
    CouponDTO deactivate(Long id);
}
