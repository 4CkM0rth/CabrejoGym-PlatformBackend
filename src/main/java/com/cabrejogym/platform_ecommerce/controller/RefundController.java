package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AdminRefundDecisionRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateRefundRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.RefundDTO;
import com.cabrejogym.platform_ecommerce.application.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public RefundDTO createMyRefund(Authentication auth, @Valid @RequestBody CreateRefundRequest request) {
        return refundService.createMyRefund(auth.getName(), request);
    }

    @GetMapping("/me")
    public List<RefundDTO> listMyRefunds(Authentication auth) {
        return refundService.listMyRefunds(auth.getName());
    }

    @GetMapping("/me/{id}")
    public RefundDTO getMyRefundById(@PathVariable Long id, Authentication auth) {
        return refundService.getMyRefundById(auth.getName(), id);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public RefundDTO approve(@PathVariable Long id, @Valid @RequestBody AdminRefundDecisionRequest request) {
        return refundService.approve(id, request);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public RefundDTO reject(@PathVariable Long id, @Valid @RequestBody AdminRefundDecisionRequest request) {
        return refundService.reject(id, request);
    }

    @PatchMapping("/{id}/refunded")
    @PreAuthorize("hasRole('ADMIN')")
    public RefundDTO markAsRefunded(@PathVariable Long id, @Valid @RequestBody AdminRefundDecisionRequest request) {
        return refundService.markAsRefunded(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<RefundDTO> listAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return refundService.listAll(page, size);
    }
}
