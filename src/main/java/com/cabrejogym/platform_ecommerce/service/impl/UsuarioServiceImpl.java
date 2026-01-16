package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.dtos.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;
import com.cabrejogym.platform_ecommerce.dtos.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.dtos.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.dtos.ResetPasswordRequest;
import com.cabrejogym.platform_ecommerce.exceptions.ConflictoException;
import com.cabrejogym.platform_ecommerce.exceptions.RecursoNoEncontradoException;
import com.cabrejogym.platform_ecommerce.model.Usuario;
import com.cabrejogym.platform_ecommerce.repository.UsuarioRepository;
import com.cabrejogym.platform_ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
        return toDto(usuario);
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        if (!existente.getEmail().equals(dto.email()) && usuarioRepository.existsByEmail(dto.email())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + dto.email());
        }

        existente.setNombre(dto.nombre());
        existente.setApellido(dto.apellido());
        existente.setFechaNacimiento(dto.fechaNacimiento());

        Usuario actualizado = usuarioRepository.save(existente);
        return toDto(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioDTO obtenerMiPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con email: " + email));
        return toDto(usuario);
    }

    @Override
    public UsuarioDTO actualizarMiPerfil(String email, MeUpdateRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con email: " + email));

        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setFechaNacimiento(request.fechaNacimiento());

        Usuario actualizado = usuarioRepository.save(usuario);
        return toDto(actualizado);
    }

    @Override
    public void cambiarMiPassword(String email, ChangePasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con email: " + email));

        if (usuario.getPassword() == null || !passwordEncoder.matches(request.actualPassword(), usuario.getPassword())) {
            throw new ConflictoException("La contraseña actual no es correcta");
        }

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);
    }

    private UsuarioDTO toDto(Usuario u) {
        return new UsuarioDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getFechaNacimiento()
        );
    }

    @Override
    public UsuarioDTO cambiarEmailAdmin(Long id, ChangeEmailRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        // evitar conflicto
        if (!usuario.getEmail().equals(request.email()) && usuarioRepository.existsByEmail(request.email())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + request.email());
        }

        usuario.setEmail(request.email());
        return toDto(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDTO cambiarRoleAdmin(Long id, ChangeRoleRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        usuario.setRole(request.role());
        return toDto(usuarioRepository.save(usuario));
    }

    @Override
    public void resetPasswordAdmin(Long id, ResetPasswordRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);
    }

}
