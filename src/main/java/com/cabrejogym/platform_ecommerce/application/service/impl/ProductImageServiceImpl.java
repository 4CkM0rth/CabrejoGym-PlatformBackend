package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductImageRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductImageDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.product.ProductImageMapper;
import com.cabrejogym.platform_ecommerce.application.service.FileStorageService;
import com.cabrejogym.platform_ecommerce.application.service.ProductImageService;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.ProductImage;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductImageRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper imageMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ProductImageDTO addImage(Long productId, CreateProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(request.url());
        image.setAltText(request.altText());
        image.setDisplayOrder(request.displayOrder() != null ? request.displayOrder() : 0);
        image.setIsPrimary(request.isPrimary() != null ? request.isPrimary() : false);

        if (image.getIsPrimary()) {
            imageRepository.findByProductId(productId).forEach(img -> {
                img.setIsPrimary(false);
                imageRepository.save(img);
            });
        }

        ProductImage saved = imageRepository.save(image);
        return imageMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ProductImageDTO uploadImage(Long productId, MultipartFile file, String altText, Integer displayOrder, Boolean isPrimary) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Subir archivo y obtener URL
        String imageUrl = fileStorageService.storeFile(file, "products");

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(imageUrl);
        image.setAltText(altText);
        image.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        image.setIsPrimary(isPrimary != null ? isPrimary : false);

        if (image.getIsPrimary()) {
            imageRepository.findByProductId(productId).forEach(img -> {
                img.setIsPrimary(false);
                imageRepository.save(img);
            });
        }

        ProductImage saved = imageRepository.save(image);
        return imageMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Eliminar archivo físico
        try {
            fileStorageService.deleteFile(image.getUrl());
        } catch (Exception e) {
            // Log error but continue with database deletion
        }

        imageRepository.deleteById(imageId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageDTO getImageById(Long imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return imageMapper.toDTO(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageDTO> getImagesByProductId(Long productId) {
        return imageMapper.toDTOList(imageRepository.findByProductIdOrderByDisplayOrder(productId));
    }

    @Override
    @Transactional
    public ProductImageDTO setPrimaryImage(Long imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        imageRepository.findByProductId(image.getProduct().getId()).forEach(img -> {
            img.setIsPrimary(false);
            imageRepository.save(img);
        });

        image.setIsPrimary(true);
        ProductImage updated = imageRepository.save(image);
        return imageMapper.toDTO(updated);
    }
}
