package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDTO> allProducts();
    Page<ProductDTO> listAll(int page, int size);

    ProductDTO create(CreateProductRequest request);
    ProductDTO getProductById(Long id);
    ProductDTO getProductBySlug(String slug);
    ProductDTO update(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
    ProductDTO updateStock(Long id, int stock);
    
    // Catalog features
    Page<ProductDTO> searchProducts(String query, int page, int size);
    Page<ProductDTO> filterProducts(Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice, 
                                     PublicationStatus status, Boolean inStock, int page, int size);
    ProductDTO publishProduct(Long id);
    ProductDTO unpublishProduct(Long id);
    ProductDTO addTagToProduct(Long productId, Long tagId);
    void removeTagFromProduct(Long productId, Long tagId);
}
