package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.RegisterRequest;
import com.cabrejogym.platform_ecommerce.dtos.UsuarioDTO;

public interface AuthService {
    UsuarioDTO register(RegisterRequest request);
}
