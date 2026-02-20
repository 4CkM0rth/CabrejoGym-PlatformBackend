package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchReviewRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchReviewDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.branch.BranchReviewMapper;
import com.cabrejogym.platform_ecommerce.application.service.BranchReviewService;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import com.cabrejogym.platform_ecommerce.domain.entity.BranchReview;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.domain.enums.ReviewStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchReviewRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BranchReviewServiceImpl implements BranchReviewService {
    private final BranchReviewRepository reviewRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final BranchReviewMapper reviewMapper;

    @Override
    @Transactional
    public BranchReviewDTO createReview(String userEmail, Long branchId, CreateBranchReviewRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (reviewRepository.existsByBranchIdAndUserId(branchId, user.getId())) {
            throw new ConflictException("Ya has dejado una reseña para esta sede");
        }

        BranchReview review = BranchReview.builder()
                .branch(branch)
                .user(user)
                .rating(request.rating())
                .comment(request.comment())
                .status(ReviewStatus.PENDING)
                .build();

        BranchReview saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchReviewDTO> getApprovedReviewsByBranchId(Long branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findByBranchIdAndStatus(branchId, ReviewStatus.APPROVED, pageable)
                .map(reviewMapper::toDto);
    }

    @Override
    @Transactional
    public BranchReviewDTO approveReview(Long reviewId) {
        BranchReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada"));

        review.setStatus(ReviewStatus.APPROVED);
        BranchReview saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BranchReviewDTO rejectReview(Long reviewId) {
        BranchReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada"));

        review.setStatus(ReviewStatus.REJECTED);
        BranchReview saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Reseña no encontrada");
        }
        reviewRepository.deleteById(reviewId);
    }
}
