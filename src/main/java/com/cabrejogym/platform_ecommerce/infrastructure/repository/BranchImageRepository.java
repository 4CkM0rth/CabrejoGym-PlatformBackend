package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.BranchImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchImageRepository extends JpaRepository<BranchImage, Long> {
    List<BranchImage> findByBranchIdOrderByDisplayOrder(Long branchId);
    List<BranchImage> findByBranchId(Long branchId);
}
