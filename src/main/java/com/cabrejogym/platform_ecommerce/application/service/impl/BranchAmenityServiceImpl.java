package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchAmenityRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchAmenityDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.branch.BranchAmenityMapper;
import com.cabrejogym.platform_ecommerce.application.service.BranchAmenityService;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import com.cabrejogym.platform_ecommerce.domain.entity.BranchAmenity;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchAmenityRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchAmenityServiceImpl implements BranchAmenityService {
    private final BranchAmenityRepository amenityRepository;
    private final BranchRepository branchRepository;
    private final BranchAmenityMapper amenityMapper;

    @Override
    @Transactional
    public BranchAmenityDTO addAmenity(Long branchId, CreateBranchAmenityRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada"));

        BranchAmenity amenity = BranchAmenity.builder()
                .branch(branch)
                .name(request.name())
                .description(request.description())
                .iconName(request.iconName())
                .available(request.available() != null ? request.available() : true)
                .build();

        BranchAmenity saved = amenityRepository.save(amenity);
        return amenityMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchAmenityDTO> getAmenitiesByBranchId(Long branchId) {
        return amenityRepository.findByBranchId(branchId)
                .stream()
                .map(amenityMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BranchAmenityDTO updateAmenity(Long amenityId, CreateBranchAmenityRequest request) {
        BranchAmenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenidad no encontrada"));

        if (request.name() != null) amenity.setName(request.name());
        if (request.description() != null) amenity.setDescription(request.description());
        if (request.iconName() != null) amenity.setIconName(request.iconName());
        if (request.available() != null) amenity.setAvailable(request.available());

        BranchAmenity saved = amenityRepository.save(amenity);
        return amenityMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteAmenity(Long amenityId) {
        if (!amenityRepository.existsById(amenityId)) {
            throw new ResourceNotFoundException("Amenidad no encontrada");
        }
        amenityRepository.deleteById(amenityId);
    }
}
