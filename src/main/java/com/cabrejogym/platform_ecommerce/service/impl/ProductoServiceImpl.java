package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.ProductoDTO;
import com.cabrejogym.platform_ecommerce.exceptions.RecursoNoEncontradoException;
import com.cabrejogym.platform_ecommerce.model.Producto;
import com.cabrejogym.platform_ecommerce.repository.ProductoRepository;
import com.cabrejogym.platform_ecommerce.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = new Producto(null, dto.nombre(), dto.descripcion());
        return toDto(productoRepository.save(producto));
    }

    @Override
    public List<ProductoDTO> listar() {
        return productoRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ProductoDTO obtenerPorId(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
        return toDto(p);
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));

        existente.setNombre(dto.nombre());
        existente.setDescripcion(dto.descripcion());

        return toDto(productoRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO toDto(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getDescripcion());
    }
}
