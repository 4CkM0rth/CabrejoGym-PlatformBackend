package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.BranchAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchAmenityRepository extends JpaRepository<BranchAmenity, Long> {
    List<BranchAmenity> findByBranchId(Long branchId);
    List<BranchAmenity> findByBranchIdAndAvailableTrue(Long branchId);
}
