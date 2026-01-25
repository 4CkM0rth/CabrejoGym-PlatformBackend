package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.ProductDTO;
import com.cabrejogym.platform_ecommerce.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.model.Product;
import com.cabrejogym.platform_ecommerce.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {

        Product product = new Product(
                null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.hasDiscount(),
                dto.discountPercent()
        );

        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> allProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toDto(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        product.setName(dto.name());
        product.setDescription(dto.description());

        Product updated = productRepository.save(product);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getHasDiscount(),
                product.getDiscountPercent()
        );
    }
}
