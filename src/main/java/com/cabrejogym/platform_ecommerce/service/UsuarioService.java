package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.dtos.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;
import com.cabrejogym.platform_ecommerce.dtos.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.dtos.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.dtos.ResetPasswordRequest;


import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> listar();
    UsuarioDTO obtenerPorId(Long id);
    UsuarioDTO actualizar(Long id, UsuarioDTO dto);
    void eliminar(Long id);

    UsuarioDTO obtenerMiPerfil(String email);
    UsuarioDTO actualizarMiPerfil(String email, MeUpdateRequest request);
    void cambiarMiPassword(String email, ChangePasswordRequest request);

    UsuarioDTO cambiarEmailAdmin(Long id, ChangeEmailRequest request);
    UsuarioDTO cambiarRoleAdmin(Long id, ChangeRoleRequest request);
    void resetPasswordAdmin(Long id, ResetPasswordRequest request);

}
