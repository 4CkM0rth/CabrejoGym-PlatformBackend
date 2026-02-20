package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.BranchReview;
import com.cabrejogym.platform_ecommerce.domain.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchReviewRepository extends JpaRepository<BranchReview, Long> {
    Page<BranchReview> findByBranchIdAndStatus(Long branchId, ReviewStatus status, Pageable pageable);
    List<BranchReview> findByBranchIdAndStatus(Long branchId, ReviewStatus status);
    Optional<BranchReview> findByBranchIdAndUserId(Long branchId, Long userId);
    boolean existsByBranchIdAndUserId(Long branchId, Long userId);
    
    @Query("SELECT AVG(r.rating) FROM BranchReview r WHERE r.branch.id = :branchId AND r.status = 'APPROVED'")
    Double getAverageRatingByBranchId(Long branchId);
    
    @Query("SELECT COUNT(r) FROM BranchReview r WHERE r.branch.id = :branchId AND r.status = 'APPROVED'")
    Long countApprovedReviewsByBranchId(Long branchId);
}
