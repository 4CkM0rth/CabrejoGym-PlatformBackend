package com.cabrejogym.platform_ecommerce.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateAddressRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.AddressDTO;
import com.cabrejogym.platform_ecommerce.application.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO create(@Valid @RequestBody CreateAddressRequest request, Authentication auth) {
        return addressService.create(auth.getName(), request);
    }

    @GetMapping
    public List<AddressDTO> listMyAddresses(Authentication auth) {
        return addressService.listMyAddresses(auth.getName());
    }

    @GetMapping("/{id}")
    public AddressDTO getById(@PathVariable Long id, Authentication auth) {
        return addressService.getMyAddressById(auth.getName(), id);
    }

    @PutMapping("/{id}")
    public AddressDTO update(@PathVariable Long id,
                            @Valid @RequestBody CreateAddressRequest request,
                            Authentication auth) {
        return addressService.update(auth.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication auth) {
        addressService.delete(auth.getName(), id);
    }

    @PatchMapping("/{id}/default")
    public AddressDTO setAsDefault(@PathVariable Long id, Authentication auth) {
        return addressService.setAsDefault(auth.getName(), id);
    }
}
