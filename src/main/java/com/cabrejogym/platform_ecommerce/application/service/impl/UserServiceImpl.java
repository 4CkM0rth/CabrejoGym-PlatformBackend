package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.UserDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ResetPasswordRequest;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import com.cabrejogym.platform_ecommerce.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> all() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return toDto(user);
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!existing.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Ya existe un usuario con el email: " + dto.email());
        }

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setBirthDate(dto.birthDate());

        User updated = userRepository.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return toDto(user);
    }

    @Override
    public UserDTO updateMyProfile(String email, MeUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setBirthDate(request.birthDate());

        User updated = userRepository.save(user);
        return toDto(updated);
    }

    @Override
    public void changeMyPassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        if (user.getPassword() == null || !passwordEncoder.matches(request.updatePassword(), user.getPassword())) {
            throw new ConflictException("La contraseña actual no es correcta");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private UserDTO toDto(User u) {
        return new UserDTO(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getBirthDate()
        );
    }

    @Override
    public UserDTO changeEmailAdmin(Long id, ChangeEmailRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Ya existe un usuario con el email: " + request.email());
        }

        user.setEmail(request.email());
        return toDto(userRepository.save(user));
    }

    @Override
    public UserDTO changeRoleAdmin(Long id, ChangeRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setRole(request.role());
        return toDto(userRepository.save(user));
    }

    @Override
    public void resetPasswordAdmin(Long id, ResetPasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

}
