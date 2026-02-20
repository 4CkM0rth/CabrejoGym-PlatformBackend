package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.*;
import com.cabrejogym.platform_ecommerce.application.dtos.response.*;
import com.cabrejogym.platform_ecommerce.application.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchImageService branchImageService;
    private final BranchAmenityService branchAmenityService;
    private final BranchReviewService branchReviewService;
    private final MembershipPlanService membershipPlanService;

    // Público (info sedes)
    @GetMapping
    public List<BranchDTO> listActive() {
        return branchService.listActive();
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO getByIdAdmin(@PathVariable Long id) {
        return branchService.getById(id);
    }

    // Admin
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BranchDTO> listAll() {
        return branchService.listAll();
    }

    @GetMapping("/{id}")
    public BranchDTO getById(@PathVariable Long id) {
        return branchService.getActiveById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchDTO create(@Valid @RequestBody CreateBranchRequest request) {
        return branchService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO update(@PathVariable Long id, @Valid @RequestBody UpdateBranchRequest request) {
        return branchService.update(id, request);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO activate(@PathVariable Long id) {
        return branchService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO deactivate(@PathVariable Long id) {
        return branchService.deactivate(id);
    }

    // ==================== IMÁGENES ====================
    @PostMapping("/{id}/images")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchImageDTO addImage(@PathVariable Long id, @Valid @RequestBody CreateBranchImageRequest request) {
        return branchImageService.addImage(id, request);
    }

    @PostMapping(value = "/{id}/images/upload", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchImageDTO uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                       @RequestParam(required = false) String altText,
                                       @RequestParam(required = false) Integer displayOrder,
                                       @RequestParam(required = false) Boolean isPrimary) {
        return branchImageService.uploadImage(id, file, altText, displayOrder, isPrimary);
    }

    @GetMapping("/{id}/images")
    public List<BranchImageDTO> getImages(@PathVariable Long id) {
        return branchImageService.getImagesByBranchId(id);
    }

    @PostMapping("/images/{imageId}/set-primary")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchImageDTO setPrimaryImage(@PathVariable Long imageId) {
        return branchImageService.setPrimaryImage(imageId);
    }

    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Long imageId) {
        branchImageService.deleteImage(imageId);
    }

    // ==================== AMENIDADES ====================
    @PostMapping("/{id}/amenities")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchAmenityDTO addAmenity(@PathVariable Long id, @Valid @RequestBody CreateBranchAmenityRequest request) {
        return branchAmenityService.addAmenity(id, request);
    }

    @GetMapping("/{id}/amenities")
    public List<BranchAmenityDTO> getAmenities(@PathVariable Long id) {
        return branchAmenityService.getAmenitiesByBranchId(id);
    }

    @PutMapping("/amenities/{amenityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchAmenityDTO updateAmenity(@PathVariable Long amenityId, @Valid @RequestBody CreateBranchAmenityRequest request) {
        return branchAmenityService.updateAmenity(amenityId, request);
    }

    @DeleteMapping("/amenities/{amenityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAmenity(@PathVariable Long amenityId) {
        branchAmenityService.deleteAmenity(amenityId);
    }

    // ==================== RESEÑAS ====================
    @PostMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchReviewDTO createReview(@PathVariable Long id, @Valid @RequestBody CreateBranchReviewRequest request, Authentication auth) {
        return branchReviewService.createReview(auth.getName(), id, request);
    }

    @GetMapping("/{id}/reviews")
    public Page<BranchReviewDTO> getReviews(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        return branchReviewService.getApprovedReviewsByBranchId(id, page, size);
    }

    @PatchMapping("/reviews/{reviewId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchReviewDTO approveReview(@PathVariable Long reviewId) {
        return branchReviewService.approveReview(reviewId);
    }

    @PatchMapping("/reviews/{reviewId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchReviewDTO rejectReview(@PathVariable Long reviewId) {
        return branchReviewService.rejectReview(reviewId);
    }

    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long reviewId) {
        branchReviewService.deleteReview(reviewId);
    }

    // ==================== PLANES DE MEMBRESÍA ====================
    @PostMapping("/{id}/membership-plans")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipPlanDTO createPlan(@PathVariable Long id, @Valid @RequestBody CreateMembershipPlanRequest request) {
        return membershipPlanService.createPlan(id, request);
    }

    @GetMapping("/{id}/membership-plans")
    public List<MembershipPlanDTO> getPlans(@PathVariable Long id) {
        return membershipPlanService.getActivePlansByBranchId(id);
    }

    @GetMapping("/{id}/membership-plans/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MembershipPlanDTO> getAllPlans(@PathVariable Long id) {
        return membershipPlanService.getAllPlansByBranchId(id);
    }

    @PutMapping("/membership-plans/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MembershipPlanDTO updatePlan(@PathVariable Long planId, @Valid @RequestBody UpdateMembershipPlanRequest request) {
        return membershipPlanService.updatePlan(planId, request);
    }

    @DeleteMapping("/membership-plans/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable Long planId) {
        membershipPlanService.deletePlan(planId);
    }
}
