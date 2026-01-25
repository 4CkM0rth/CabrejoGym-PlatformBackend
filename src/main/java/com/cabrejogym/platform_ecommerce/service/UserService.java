package com.cabrejogym.platform_ecommerce.service;

import com.cabrejogym.platform_ecommerce.dtos.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.dtos.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.dtos.UserDTO;
import com.cabrejogym.platform_ecommerce.dtos.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.dtos.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.dtos.ResetPasswordRequest;


import java.util.List;

public interface UserService {
    List<UserDTO> all();
    UserDTO getById(Long id);
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);

    UserDTO getMyProfile(String email);
    UserDTO updateMyProfile(String email, MeUpdateRequest request);
    void changeMyPassword(String email, ChangePasswordRequest request);

    UserDTO changeEmailAdmin(Long id, ChangeEmailRequest request);
    UserDTO changeRoleAdmin(Long id, ChangeRoleRequest request);
    void resetPasswordAdmin(Long id, ResetPasswordRequest request);

}
