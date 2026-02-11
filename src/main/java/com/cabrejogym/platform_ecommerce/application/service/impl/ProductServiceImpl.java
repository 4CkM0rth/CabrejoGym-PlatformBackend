package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.product.ProductMapper;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.BadRequestException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO create(CreateProductRequest request) {
        validateDiscount(request.hasDiscount(), request.discountPercent());

        Product product = productMapper.toEntity(request);
        normalizeDiscountFields(product);

        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        validateDiscount(request.hasDiscount(), request.discountPercent());

        productMapper.updateEntity(product, request);
        normalizeDiscountFields(product);

        Product updated = productRepository.save(product);
        return productMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductDTO updateStock(Long id, int stock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (stock < 0) {
            throw new BadRequestException("El stock no puede ser negativo");
        }

        product.setStock(stock);
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> allProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> listAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int normalizedSize = (size <= 0) ? DEFAULT_PAGE_SIZE : size;
        int safeSize = Math.min(normalizedSize, MAX_PAGE_SIZE);

        PageRequest pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    private void validateDiscount(Boolean hasDiscount, BigDecimal discountPercent) {
        if (hasDiscount == null) {
            throw new BadRequestException("hasDiscount no puede ser null");
        }
        if (discountPercent == null) {
            throw new BadRequestException("discountPercent no puede ser null");
        }

        if (hasDiscount) {
            if (discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("discountPercent debe ser > 0 cuando hasDiscount=true");
            }
            if (discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new BadRequestException("discountPercent debe ser <= 100 cuando hasDiscount=true");
            }
        } else {
            if (discountPercent.compareTo(BigDecimal.ZERO) != 0) {
                throw new BadRequestException("discountPercent debe ser 0 cuando hasDiscount=false");
            }
        }
    }

    private void normalizeDiscountFields(Product product) {
        if (!Boolean.TRUE.equals(product.getHasDiscount())) {
            product.setHasDiscount(false);
            product.setDiscountPercent(BigDecimal.ZERO);
        } else if (product.getDiscountPercent() == null) {
            product.setDiscountPercent(BigDecimal.ZERO);
        }
    }
}
