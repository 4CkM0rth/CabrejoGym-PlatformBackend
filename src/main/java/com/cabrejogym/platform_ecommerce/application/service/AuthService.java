package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.response.AuthResponse;
import com.cabrejogym.platform_ecommerce.application.dtos.request.LoginRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.RegisterRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.UserDTO;

public interface AuthService {
    UserDTO register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
