package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateCategoryRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateCategoryRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryRequest request);
    CategoryDTO updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO getCategoryBySlug(String slug);
    List<CategoryDTO> getAllCategories();
    List<CategoryDTO> getActiveCategories();
    List<CategoryDTO> getRootCategories();
    List<CategoryDTO> getSubcategories(Long parentId);
}
