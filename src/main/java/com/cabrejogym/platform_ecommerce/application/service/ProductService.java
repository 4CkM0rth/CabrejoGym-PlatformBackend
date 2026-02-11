package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<ProductDTO> allProducts();
    Page<ProductDTO> listAll(int page, int size);

    ProductDTO create(CreateProductRequest request);
    ProductDTO getProductById(Long id);
    ProductDTO update(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
    ProductDTO updateStock(Long id, int stock);
}
