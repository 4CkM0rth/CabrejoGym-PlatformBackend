package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.AuthResponse;
import com.cabrejogym.platform_ecommerce.dtos.LoginRequest;
import com.cabrejogym.platform_ecommerce.dtos.RegisterRequest;
import com.cabrejogym.platform_ecommerce.dtos.UserDTO;

public interface AuthService {
    UserDTO register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
