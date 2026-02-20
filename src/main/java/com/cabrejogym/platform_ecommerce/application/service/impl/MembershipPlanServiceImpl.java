package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateMembershipPlanRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateMembershipPlanRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.MembershipPlanDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.branch.MembershipPlanMapper;
import com.cabrejogym.platform_ecommerce.application.service.MembershipPlanService;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import com.cabrejogym.platform_ecommerce.domain.entity.MembershipPlan;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {
    private final MembershipPlanRepository planRepository;
    private final BranchRepository branchRepository;
    private final MembershipPlanMapper planMapper;

    @Override
    @Transactional
    public MembershipPlanDTO createPlan(Long branchId, CreateMembershipPlanRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada"));

        MembershipPlan plan = MembershipPlan.builder()
                .branch(branch)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .durationMonths(request.durationMonths())
                .isPopular(request.isPopular() != null ? request.isPopular() : false)
                .active(true)
                .displayOrder(request.displayOrder() != null ? request.displayOrder() : 0)
                .build();

        MembershipPlan saved = planRepository.save(plan);
        return planMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlanDTO> getActivePlansByBranchId(Long branchId) {
        return planRepository.findByBranchIdAndActiveTrueOrderByDisplayOrder(branchId)
                .stream()
                .map(planMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlanDTO> getAllPlansByBranchId(Long branchId) {
        return planRepository.findByBranchIdOrderByDisplayOrder(branchId)
                .stream()
                .map(planMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public MembershipPlanDTO updatePlan(Long planId, UpdateMembershipPlanRequest request) {
        MembershipPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado"));

        if (request.name() != null) plan.setName(request.name());
        if (request.description() != null) plan.setDescription(request.description());
        if (request.price() != null) plan.setPrice(request.price());
        if (request.durationMonths() != null) plan.setDurationMonths(request.durationMonths());
        if (request.isPopular() != null) plan.setIsPopular(request.isPopular());
        if (request.active() != null) plan.setActive(request.active());
        if (request.displayOrder() != null) plan.setDisplayOrder(request.displayOrder());

        MembershipPlan saved = planRepository.save(plan);
        return planMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deletePlan(Long planId) {
        if (!planRepository.existsById(planId)) {
            throw new ResourceNotFoundException("Plan no encontrado");
        }
        planRepository.deleteById(planId);
    }
}
