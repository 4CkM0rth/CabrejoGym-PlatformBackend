package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository  extends JpaRepository<Branch, Long> {

    List<Branch> findByActiveTrueOrderByCityAscNameAsc();
}
