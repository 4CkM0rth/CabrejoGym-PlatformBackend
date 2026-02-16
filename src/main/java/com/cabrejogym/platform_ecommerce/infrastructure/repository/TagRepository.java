package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findBySlug(String slug);
    
    List<Tag> findByNameIn(List<String> names);
    
    boolean existsBySlug(String slug);
}
