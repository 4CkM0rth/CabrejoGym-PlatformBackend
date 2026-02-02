package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangePasswordRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.MeUpdateRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.UserDTO;
import com.cabrejogym.platform_ecommerce.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    @GetMapping
    public UserDTO me(Authentication authentication) {
        String email = authentication.getName();
        return userService.getMyProfile(email);
    }

    @PutMapping
    public UserDTO updated(@Valid @RequestBody MeUpdateRequest request, Authentication authentication) {
        String email = authentication.getName();
        return userService.updateMyProfile(email, request);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        String email = authentication.getName();
        userService.changeMyPassword(email, request);
    }

}
