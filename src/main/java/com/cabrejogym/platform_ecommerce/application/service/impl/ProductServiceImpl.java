package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateProductRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.ProductDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.product.ProductMapper;
import com.cabrejogym.platform_ecommerce.application.util.SlugUtil;
import com.cabrejogym.platform_ecommerce.domain.enums.PublicationStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.BadRequestException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.domain.entity.*;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.*;
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
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final TagRepository tagRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO create(CreateProductRequest request) {
        validateDiscount(request.hasDiscount(), request.discountPercent());

        Product product = new Product();
        product.setName(request.name());
        product.setSlug(SlugUtil.toSlug(request.name()));
        product.setDescription(request.description());
        product.setShortDescription(request.shortDescription());
        product.setPrice(request.price());
        product.setHasDiscount(request.hasDiscount());
        product.setDiscountPercent(request.discountPercent());
        product.setStock(request.stock());
        product.setHasVariants(request.hasVariants() != null ? request.hasVariants() : false);
        product.setStatus(PublicationStatus.DRAFT);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
            product.setBrand(brand);
        }

        normalizeDiscountFields(product);

        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con slug: " + slug));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (request.hasDiscount() != null && request.discountPercent() != null) {
            validateDiscount(request.hasDiscount(), request.discountPercent());
        }

        if (request.name() != null) {
            product.setName(request.name());
            product.setSlug(SlugUtil.toSlug(request.name()));
        }
        if (request.description() != null) product.setDescription(request.description());
        if (request.shortDescription() != null) product.setShortDescription(request.shortDescription());
        if (request.price() != null) product.setPrice(request.price());
        if (request.hasDiscount() != null) product.setHasDiscount(request.hasDiscount());
        if (request.discountPercent() != null) product.setDiscountPercent(request.discountPercent());
        if (request.stock() != null) product.setStock(request.stock());
        if (request.status() != null) product.setStatus(request.status());
        if (request.hasVariants() != null) product.setHasVariants(request.hasVariants());

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
            product.setBrand(brand);
        }

        normalizeDiscountFields(product);

        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
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
        return productMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> allProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
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

        return productRepository.findAll(pageable).map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> searchProducts(String query, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return productRepository.searchByNameOrDescription(query, pageable).map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> filterProducts(Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice,
                                            PublicationStatus status, Boolean inStock, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return productRepository.filterProducts(categoryId, brandId, minPrice, maxPrice, status, inStock, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional
    public ProductDTO publishProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        product.setStatus(PublicationStatus.PUBLISHED);
        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public ProductDTO unpublishProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        product.setStatus(PublicationStatus.DRAFT);
        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public ProductDTO addTagToProduct(Long productId, Long tagId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        ProductTag productTag = new ProductTag();
        productTag.setProduct(product);
        productTag.setTag(tag);
        product.getTags().add(productTag);

        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void removeTagFromProduct(Long productId, Long tagId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.getTags().removeIf(pt -> pt.getTag().getId().equals(tagId));
        productRepository.save(product);
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
