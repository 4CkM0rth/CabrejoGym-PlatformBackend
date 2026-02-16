package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateCategoryRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateCategoryRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CategoryDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.category.CategoryMapper;
import com.cabrejogym.platform_ecommerce.application.service.CategoryService;
import com.cabrejogym.platform_ecommerce.application.util.SlugUtil;
import com.cabrejogym.platform_ecommerce.domain.entity.Category;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(SlugUtil.toSlug(request.name()));
        category.setDescription(request.description());
        category.setDisplayOrder(request.displayOrder() != null ? request.displayOrder() : 0);
        category.setActive(true);

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (request.name() != null) {
            category.setName(request.name());
            category.setSlug(SlugUtil.toSlug(request.name()));
        }
        if (request.description() != null) {
            category.setDescription(request.description());
        }
        if (request.displayOrder() != null) {
            category.setDisplayOrder(request.displayOrder());
        }
        if (request.active() != null) {
            category.setActive(request.active());
        }
        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        Category updated = categoryRepository.save(category);
        return categoryMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDTOList(categoryRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getActiveCategories() {
        return categoryMapper.toDTOList(categoryRepository.findByActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getRootCategories() {
        return categoryMapper.toDTOList(categoryRepository.findByParentIsNull());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getSubcategories(Long parentId) {
        return categoryMapper.toDTOList(categoryRepository.findByParentId(parentId));
    }
}
