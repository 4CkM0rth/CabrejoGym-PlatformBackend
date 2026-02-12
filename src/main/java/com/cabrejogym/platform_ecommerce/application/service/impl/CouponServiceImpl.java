package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateCouponRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CouponDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.coupon.CouponMapper;
import com.cabrejogym.platform_ecommerce.application.service.CouponService;
import com.cabrejogym.platform_ecommerce.domain.entity.Coupon;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponDTO create(CreateCouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.code().toUpperCase());
        coupon.setType(request.type());
        coupon.setValue(request.value());
        coupon.setMinPurchase(request.minPurchase());
        coupon.setMaxDiscount(request.maxDiscount());
        coupon.setUsageLimit(request.usageLimit());
        coupon.setValidFrom(request.validFrom());
        coupon.setValidUntil(request.validUntil());
        coupon.setActive(true);

        Coupon saved = couponRepository.save(coupon);
        return couponMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDTO getByCode(String code) {
        Coupon coupon = couponRepository.findByCodeAndActiveTrue(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado: " + code));
        return couponMapper.toDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDTO validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCodeAndActiveTrue(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado: " + code));

        Instant now = Instant.now();
        
        if (!coupon.getActive()) {
            throw new ConflictException("El cupón no está activo");
        }
        
        if (now.isBefore(coupon.getValidFrom())) {
            throw new ConflictException("El cupón aún no es válido");
        }
        
        if (now.isAfter(coupon.getValidUntil())) {
            throw new ConflictException("El cupón ha expirado");
        }
        
        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new ConflictException("El cupón ha alcanzado su límite de uso");
        }

        return couponMapper.toDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponDTO> listAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        return couponRepository.findAll(
                        PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"))
                )
                .map(couponMapper::toDto);
    }

    @Override
    @Transactional
    public CouponDTO deactivate(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));
        
        coupon.setActive(false);
        Coupon saved = couponRepository.save(coupon);
        return couponMapper.toDto(saved);
    }
}
