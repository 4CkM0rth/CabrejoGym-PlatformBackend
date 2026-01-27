package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.dtos.ProductDTO;
import com.cabrejogym.platform_ecommerce.dtos.UpdateStockRequest;
import com.cabrejogym.platform_ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> all() {
        return productService.allProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@Valid @RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    @PatchMapping("/{id}/stock")
    public ProductDTO updateStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request) {
        return productService.updateStock(id, request.stock());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
