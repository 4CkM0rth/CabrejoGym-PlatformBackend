package com.cabrejogym.platform_ecommerce.application.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductImageRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductVariantRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductVariantRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateStockRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductImageDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductVariantDTO;
import com.cabrejogym.platform_ecommerce.application.service.ProductImageService;
import com.cabrejogym.platform_ecommerce.application.service.ProductService;
import com.cabrejogym.platform_ecommerce.application.service.ProductVariantService;
import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductVariantService variantService;
    private final ProductImageService imageService;

    @GetMapping
    public Page<ProductDTO> listAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        return productService.listAll(page, size);
    }

    @GetMapping("/all")
    public List<ProductDTO> all() {
        return productService.allProducts();
    }

    @GetMapping("/search")
    public Page<ProductDTO> search(@RequestParam String query,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return productService.searchProducts(query, page, size);
    }

    @GetMapping("/filter")
    public Page<ProductDTO> filter(@RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) Long brandId,
                                   @RequestParam(required = false) BigDecimal minPrice,
                                   @RequestParam(required = false) BigDecimal maxPrice,
                                   @RequestParam(required = false) PublicationStatus status,
                                   @RequestParam(required = false) Boolean inStock,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return productService.filterProducts(categoryId, brandId, minPrice, maxPrice, status, inStock, page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@Valid @RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    public ProductDTO byId(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/slug/{slug}")
    public ProductDTO bySlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return productService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/stock")
    public ProductDTO updateStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request) {
        return productService.updateStock(id, request.stock());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/publish")
    public ProductDTO publish(@PathVariable Long id) {
        return productService.publishProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/unpublish")
    public ProductDTO unpublish(@PathVariable Long id) {
        return productService.unpublishProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/tags/{tagId}")
    public ProductDTO addTag(@PathVariable Long id, @PathVariable Long tagId) {
        return productService.addTagToProduct(id, tagId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        productService.removeTagFromProduct(id, tagId);
    }

    // Variant endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/variants")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVariantDTO createVariant(@PathVariable Long id, @Valid @RequestBody CreateProductVariantRequest request) {
        return variantService.createVariant(id, request);
    }

    @GetMapping("/{id}/variants")
    public List<ProductVariantDTO> getVariants(@PathVariable Long id) {
        return variantService.getVariantsByProductId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/variants/{variantId}")
    public ProductVariantDTO updateVariant(@PathVariable Long variantId, @Valid @RequestBody UpdateProductVariantRequest request) {
        return variantService.updateVariant(variantId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/variants/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVariant(@PathVariable Long variantId) {
        variantService.deleteVariant(variantId);
    }

    // Image endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageDTO addImage(@PathVariable Long id, @Valid @RequestBody CreateProductImageRequest request) {
        return imageService.addImage(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{id}/images/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageDTO uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String altText,
            @RequestParam(required = false) Integer displayOrder,
            @RequestParam(required = false) Boolean isPrimary) {
        return imageService.uploadImage(id, file, altText, displayOrder, isPrimary);
    }

    @GetMapping("/{id}/images")
    public List<ProductImageDTO> getImages(@PathVariable Long id) {
        return imageService.getImagesByProductId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/images/{imageId}/set-primary")
    public ProductImageDTO setPrimaryImage(@PathVariable Long imageId) {
        return imageService.setPrimaryImage(imageId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
    }
}
