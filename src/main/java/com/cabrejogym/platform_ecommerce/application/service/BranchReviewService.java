package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchReviewRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchReviewDTO;
import org.springframework.data.domain.Page;

public interface BranchReviewService {
    BranchReviewDTO createReview(String userEmail, Long branchId, CreateBranchReviewRequest request);
    Page<BranchReviewDTO> getApprovedReviewsByBranchId(Long branchId, int page, int size);
    BranchReviewDTO approveReview(Long reviewId);
    BranchReviewDTO rejectReview(Long reviewId);
    void deleteReview(Long reviewId);
}
