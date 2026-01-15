package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.ProductoDTO;

import java.util.List;

public interface ProductoService {
    ProductoDTO crear(ProductoDTO dto);
    List<ProductoDTO> listar();
    ProductoDTO obtenerPorId(Long id);
    ProductoDTO actualizar(Long id, ProductoDTO dto);
    void eliminar(Long id);
}
