package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateTagRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(CreateTagRequest request);
    void deleteTag(Long id);
    TagDTO getTagById(Long id);
    TagDTO getTagBySlug(String slug);
    List<TagDTO> getAllTags();
}
