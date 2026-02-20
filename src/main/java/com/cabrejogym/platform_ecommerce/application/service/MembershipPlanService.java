package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateMembershipPlanRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateMembershipPlanRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.MembershipPlanDTO;

import java.util.List;

public interface MembershipPlanService {
    MembershipPlanDTO createPlan(Long branchId, CreateMembershipPlanRequest request);
    List<MembershipPlanDTO> getActivePlansByBranchId(Long branchId);
    List<MembershipPlanDTO> getAllPlansByBranchId(Long branchId);
    MembershipPlanDTO updatePlan(Long planId, UpdateMembershipPlanRequest request);
    void deletePlan(Long planId);
}
