package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;
import com.cabrejogym.platform_ecommerce.exceptions.ConflictoException;
import com.cabrejogym.platform_ecommerce.exceptions.RecursoNoEncontradoException;
import com.cabrejogym.platform_ecommerce.model.Usuario;
import com.cabrejogym.platform_ecommerce.repository.UsuarioRepository;
import com.cabrejogym.platform_ecommerce.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + dto.email());
        }

        Usuario usuario = new Usuario(
                null,
                dto.nombre(),
                dto.apellido(),
                dto.email(),
                dto.fechaNacimiento()
        );

        Usuario guardado = usuarioRepository.save(usuario);
        return toDto(guardado);
    }

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

        // si cambia email, validar conflicto
        if (!existente.getEmail().equals(dto.email()) && usuarioRepository.existsByEmail(dto.email())) {
            throw new ConflictoException("Ya existe un usuario con el email: " + dto.email());
        }

        existente.setNombre(dto.nombre());
        existente.setApellido(dto.apellido());
        existente.setEmail(dto.email());
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

    private UsuarioDTO toDto(Usuario u) {
        return new UsuarioDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getFechaNacimiento()
        );
    }
}
