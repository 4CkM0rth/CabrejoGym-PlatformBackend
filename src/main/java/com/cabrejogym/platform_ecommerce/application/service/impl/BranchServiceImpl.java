package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.mapper.branch.BranchMapper;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchRepository;
import com.cabrejogym.platform_ecommerce.application.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> listActive() {
        return branchRepository.findByActiveTrueOrderByCityAscNameAsc()
                .stream()
                .map(branchMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO getActiveById(Long id) {
        Branch branch = getOrThrow(id);

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new ResourceNotFoundException("Sede no encontrada con id: " + id);
        }

        return branchMapper.toDto(branch);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO getById(Long id) {
        return branchMapper.toDto(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> listAll() {
        return branchRepository.findAll()
                .stream()
                .map(branchMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BranchDTO create(CreateBranchRequest request) {
        Branch branch = branchMapper.fromCreateRequest(request);
        branch.setActive(true);
        return branchMapper.toDto(branchRepository.save(branch));
    }

    @Override
    @Transactional
    public BranchDTO update(Long id, UpdateBranchRequest request) {
        Branch branch = getOrThrow(id);
        branchMapper.updateEntity(request, branch);
        return branchMapper.toDto(branchRepository.save(branch));
    }

    @Override
    @Transactional
    public BranchDTO activate(Long id) {
        Branch branch = getOrThrow(id);
        branch.setActive(true);
        return branchMapper.toDto(branchRepository.save(branch));
    }

    @Override
    @Transactional
    public BranchDTO deactivate(Long id) {
        Branch branch = getOrThrow(id);
        branch.setActive(false);
        return branchMapper.toDto(branchRepository.save(branch));
    }

    private Branch getOrThrow(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada con id: " + id));
    }


}