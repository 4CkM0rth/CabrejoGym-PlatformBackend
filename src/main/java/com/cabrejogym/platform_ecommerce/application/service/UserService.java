package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.UserDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ResetPasswordRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface UserService {
    UserDTO getById(Long id);
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);

    UserDTO getMyProfile(String email);
    UserDTO updateMyProfile(String email, MeUpdateRequest request);
    void changeMyPassword(String email, ChangePasswordRequest request);

    UserDTO changeEmailAdmin(Long id, ChangeEmailRequest request);
    UserDTO changeRoleAdmin(Long id, ChangeRoleRequest request);
    void resetPasswordAdmin(Long id, ResetPasswordRequest request);
    Page<UserDTO> all(int page, int size);

}
