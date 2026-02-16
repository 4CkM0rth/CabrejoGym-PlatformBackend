package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductImageRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    ProductImageDTO addImage(Long productId, CreateProductImageRequest request);
    ProductImageDTO uploadImage(Long productId, MultipartFile file, String altText, Integer displayOrder, Boolean isPrimary);
    void deleteImage(Long imageId);
    ProductImageDTO getImageById(Long imageId);
    List<ProductImageDTO> getImagesByProductId(Long productId);
    ProductImageDTO setPrimaryImage(Long imageId);
}
