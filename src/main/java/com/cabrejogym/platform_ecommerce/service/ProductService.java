package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO dto);
    List<ProductDTO> allProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, ProductDTO dto);
    void deleteProduct(Long id);
    ProductDTO updateStock(Long id, int stock);

}
