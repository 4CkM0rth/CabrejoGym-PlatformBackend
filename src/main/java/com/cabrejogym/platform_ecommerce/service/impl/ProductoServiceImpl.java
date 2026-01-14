package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.ProductoDTO;
import com.cabrejogym.platform_ecommerce.model.Producto;
import com.cabrejogym.platform_ecommerce.repository.ProductoRepository;
import com.cabrejogym.platform_ecommerce.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoDTO crearProducto(ProductoDTO dto) {

        Producto producto = new Producto(
                null,
                dto.nombre(),
                dto.descripcion()
        );

        Producto guardado = productoRepository.save(producto);

        return new ProductoDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getDescripcion()
        );
    }
}
