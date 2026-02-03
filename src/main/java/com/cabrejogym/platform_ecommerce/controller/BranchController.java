package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.response.BranchDTO;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateBranchRequest;
import com.cabrejogym.platform_ecommerce.application.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    // Público (info sedes)
    @GetMapping
    public List<BranchDTO> listActive() {
        return branchService.listActive();
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO getByIdAdmin(@PathVariable Long id) {
        return branchService.getById(id);
    }

    // Admin
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BranchDTO> listAll() {
        return branchService.listAll();
    }

    @GetMapping("/{id}")
    public BranchDTO getById(@PathVariable Long id) {
        return branchService.getActiveById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchDTO create(@Valid @RequestBody CreateBranchRequest request) {
        return branchService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO update(@PathVariable Long id, @Valid @RequestBody UpdateBranchRequest request) {
        return branchService.update(id, request);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO activate(@PathVariable Long id) {
        return branchService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public BranchDTO deactivate(@PathVariable Long id) {
        return branchService.deactivate(id);
    }
}
