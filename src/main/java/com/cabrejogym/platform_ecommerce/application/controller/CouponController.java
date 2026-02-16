package com.cabrejogym.platform_ecommerce.application.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateCouponRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CouponDTO;
import com.cabrejogym.platform_ecommerce.application.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponDTO create(@Valid @RequestBody CreateCouponRequest request) {
        return couponService.create(request);
    }

    @GetMapping("/{code}")
    public CouponDTO getByCode(@PathVariable String code) {
        return couponService.getByCode(code);
    }

    @GetMapping("/{code}/validate")
    public CouponDTO validate(@PathVariable String code) {
        return couponService.validateCoupon(code);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<CouponDTO> listAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return couponService.listAll(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public CouponDTO deactivate(@PathVariable Long id) {
        return couponService.deactivate(id);
    }
}
