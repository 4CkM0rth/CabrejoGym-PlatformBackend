package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateTagRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.TagDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.tag.TagMapper;
import com.cabrejogym.platform_ecommerce.application.service.TagService;
import com.cabrejogym.platform_ecommerce.application.util.SlugUtil;
import com.cabrejogym.platform_ecommerce.domain.entity.Tag;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional
    public TagDTO createTag(CreateTagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setSlug(SlugUtil.toSlug(request.name()));

        Tag saved = tagRepository.save(tag);
        return tagMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        tagRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        return tagMapper.toDTO(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO getTagBySlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        return tagMapper.toDTO(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        return tagMapper.toDTOList(tagRepository.findAll());
    }
}
