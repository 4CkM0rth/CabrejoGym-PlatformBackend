package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateAddressRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.AddressDTO;

import java.util.List;

public interface AddressService {
    
    AddressDTO create(String email, CreateAddressRequest request);
    
    List<AddressDTO> listMyAddresses(String email);
    
    AddressDTO getMyAddressById(String email, Long id);
    
    AddressDTO update(String email, Long id, CreateAddressRequest request);
    
    void delete(String email, Long id);
    
    AddressDTO setAsDefault(String email, Long id);
}
