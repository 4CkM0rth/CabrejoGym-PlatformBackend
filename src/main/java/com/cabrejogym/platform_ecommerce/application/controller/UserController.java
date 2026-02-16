package com.cabrejogym.platform_ecommerce.application.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.response.UserDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangeEmailRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ChangeRoleRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.ResetPasswordRequest;
import com.cabrejogym.platform_ecommerce.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public UserDTO updated(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}/email")
    public UserDTO changeEmail(@PathVariable Long id, @Valid @RequestBody ChangeEmailRequest request) {
        return userService.changeEmailAdmin(id, request);
    }

    @PatchMapping("/{id}/role")
    public UserDTO changeRole(@PathVariable Long id, @Valid @RequestBody ChangeRoleRequest request) {
        return userService.changeRoleAdmin(id, request);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPasswordAdmin(id, request);
    }

    @GetMapping
    public Page<UserDTO> all(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size) {
        return userService.all(page, size);
    }
}
