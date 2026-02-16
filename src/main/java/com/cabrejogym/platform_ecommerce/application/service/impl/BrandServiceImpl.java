package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBrandRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBrandRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.BrandDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.brand.BrandMapper;
import com.cabrejogym.platform_ecommerce.application.service.BrandService;
import com.cabrejogym.platform_ecommerce.application.util.SlugUtil;
import com.cabrejogym.platform_ecommerce.domain.entity.Brand;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandDTO createBrand(CreateBrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.name());
        brand.setSlug(SlugUtil.toSlug(request.name()));
        brand.setDescription(request.description());
        brand.setLogoUrl(request.logoUrl());
        brand.setActive(true);

        Brand saved = brandRepository.save(brand);
        return brandMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public BrandDTO updateBrand(Long id, UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        if (request.name() != null) {
            brand.setName(request.name());
            brand.setSlug(SlugUtil.toSlug(request.name()));
        }
        if (request.description() != null) {
            brand.setDescription(request.description());
        }
        if (request.logoUrl() != null) {
            brand.setLogoUrl(request.logoUrl());
        }
        if (request.active() != null) {
            brand.setActive(request.active());
        }

        Brand updated = brandRepository.save(brand);
        return brandMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new RuntimeException("Brand not found");
        }
        brandRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDTO getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        return brandMapper.toDTO(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDTO getBrandBySlug(String slug) {
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        return brandMapper.toDTO(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandDTO> getAllBrands() {
        return brandMapper.toDTOList(brandRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandDTO> getActiveBrands() {
        return brandMapper.toDTOList(brandRepository.findByActiveTrue());
    }
}
