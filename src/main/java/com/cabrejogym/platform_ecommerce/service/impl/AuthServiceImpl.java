package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.RegisterRequest;
import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;
import com.cabrejogym.platform_ecommerce.exceptions.ConflictoException;
import com.cabrejogym.platform_ecommerce.model.Role;
import com.cabrejogym.platform_ecommerce.model.Usuario;
import com.cabrejogym.platform_ecommerce.repository.UsuarioRepository;
import com.cabrejogym.platform_ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO register(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + request.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setEmail(request.email());
        usuario.setFechaNacimiento(request.fechaNacimiento());

        usuario.setPassword(passwordEncoder.encode(request.password()));

        usuario.setRole(Role.USER);

        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getEmail(),
                guardado.getFechaNacimiento()
        );
    }
}
