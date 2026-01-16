package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;
import com.cabrejogym.platform_ecommerce.dtos.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.dtos.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.dtos.ResetPasswordRequest;
import com.cabrejogym.platform_ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        return usuarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @PatchMapping("/{id}/email")
    public UsuarioDTO cambiarEmail(@PathVariable Long id, @Valid @RequestBody ChangeEmailRequest request) {
        return usuarioService.cambiarEmailAdmin(id, request);
    }

    @PatchMapping("/{id}/role")
    public UsuarioDTO cambiarRole(@PathVariable Long id, @Valid @RequestBody ChangeRoleRequest request) {
        return usuarioService.cambiarRoleAdmin(id, request);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request) {
        usuarioService.resetPasswordAdmin(id, request);
    }

}
