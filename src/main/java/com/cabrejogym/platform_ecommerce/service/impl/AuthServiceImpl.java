package com.cabrejogym.platform_ecommerce.service.impl;

import com.cabrejogym.platform_ecommerce.dtos.AuthResponse;
import com.cabrejogym.platform_ecommerce.dtos.LoginRequest;
import com.cabrejogym.platform_ecommerce.dtos.RegisterRequest;
import com.cabrejogym.platform_ecommerce.dtos.UserDTO;
import com.cabrejogym.platform_ecommerce.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.model.Role;
import com.cabrejogym.platform_ecommerce.model.User;
import com.cabrejogym.platform_ecommerce.repository.UserRepository;
import com.cabrejogym.platform_ecommerce.security.JwtService;
import com.cabrejogym.platform_ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public UserDTO register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Ya existe un usuario con el email: " + request.email());
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setBirthDate(request.birthDate());

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        return new UserDTO(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getBirthDate()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateAccessToken(user);

        return new AuthResponse(token);
    }
}
