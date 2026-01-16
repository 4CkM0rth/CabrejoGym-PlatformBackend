package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.dtos.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.dtos.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;
import com.cabrejogym.platform_ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final UsuarioService usuarioService;

    @GetMapping
    public UsuarioDTO me(Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.obtenerMiPerfil(email);
    }

    @PutMapping
    public UsuarioDTO actualizar(@Valid @RequestBody MeUpdateRequest request, Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.actualizarMiPerfil(email, request);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarPassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        String email = authentication.getName();
        usuarioService.cambiarMiPassword(email, request);
    }

}
