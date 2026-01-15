package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO crear(UsuarioDTO dto);
    List<UsuarioDTO> listar();
    UsuarioDTO obtenerPorId(Long id);
    UsuarioDTO actualizar(Long id, UsuarioDTO dto);
    void eliminar(Long id);
}
