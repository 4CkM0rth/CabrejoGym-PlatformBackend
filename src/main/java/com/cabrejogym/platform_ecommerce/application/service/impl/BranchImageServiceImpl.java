package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchImageRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchImageDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.branch.BranchImageMapper;
import com.cabrejogym.platform_ecommerce.application.service.BranchImageService;
import com.cabrejogym.platform_ecommerce.application.service.FileStorageService;
import com.cabrejogym.platform_ecommerce.domain.entity.Branch;
import com.cabrejogym.platform_ecommerce.domain.entity.BranchImage;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchImageRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchImageServiceImpl implements BranchImageService {
    private final BranchImageRepository imageRepository;
    private final BranchRepository branchRepository;
    private final BranchImageMapper imageMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public BranchImageDTO addImage(Long branchId, CreateBranchImageRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada"));

        if (Boolean.TRUE.equals(request.isPrimary())) {
            resetPrimaryImages(branchId);
        }

        BranchImage image = BranchImage.builder()
                .branch(branch)
                .url(request.url())
                .altText(request.altText())
                .displayOrder(request.displayOrder() != null ? request.displayOrder() : 0)
                .isPrimary(request.isPrimary() != null ? request.isPrimary() : false)
                .build();

        BranchImage saved = imageRepository.save(image);
        return imageMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BranchImageDTO uploadImage(Long branchId, MultipartFile file, String altText, Integer displayOrder, Boolean isPrimary) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada"));

        String fileUrl = fileStorageService.storeFile(file, "branches");

        if (Boolean.TRUE.equals(isPrimary)) {
            resetPrimaryImages(branchId);
        }

        BranchImage image = BranchImage.builder()
                .branch(branch)
                .url(fileUrl)
                .altText(altText)
                .displayOrder(displayOrder != null ? displayOrder : 0)
                .isPrimary(isPrimary != null ? isPrimary : false)
                .build();

        BranchImage saved = imageRepository.save(image);
        return imageMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchImageDTO> getImagesByBranchId(Long branchId) {
        return imageRepository.findByBranchIdOrderByDisplayOrder(branchId)
                .stream()
                .map(imageMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BranchImageDTO setPrimaryImage(Long imageId) {
        BranchImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));

        resetPrimaryImages(image.getBranch().getId());
        image.setIsPrimary(true);
        BranchImage saved = imageRepository.save(image);
        return imageMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        BranchImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));

        if (image.getUrl().startsWith("http://localhost")) {
            fileStorageService.deleteFile(image.getUrl());
        }

        imageRepository.delete(image);
    }

    private void resetPrimaryImages(Long branchId) {
        List<BranchImage> images = imageRepository.findByBranchId(branchId);
        images.forEach(img -> img.setIsPrimary(false));
        imageRepository.saveAll(images);
    }
}
