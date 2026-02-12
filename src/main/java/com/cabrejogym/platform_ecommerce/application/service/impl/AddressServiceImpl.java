package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateAddressRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.AddressDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.address.AddressMapper;
import com.cabrejogym.platform_ecommerce.application.service.AddressService;
import com.cabrejogym.platform_ecommerce.domain.entity.Address;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.AddressRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDTO create(String email, CreateAddressRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (Boolean.TRUE.equals(request.isDefault())) {
            addressRepository.findByUser_EmailAndIsDefaultTrue(email)
                    .ifPresent(addr -> addr.setIsDefault(false));
        }

        Address address = new Address();
        address.setUser(user);
        address.setType(request.type());
        address.setFullName(request.fullName());
        address.setStreet(request.street());
        address.setApartment(request.apartment());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
        address.setCountry(request.country());
        address.setPhone(request.phone());
        address.setIsDefault(Boolean.TRUE.equals(request.isDefault()));

        Address saved = addressRepository.save(address);
        return addressMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> listMyAddresses(String email) {
        return addressRepository.findByUser_Email(email).stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDTO getMyAddressById(String email, Long id) {
        Address address = addressRepository.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada"));
        return addressMapper.toDto(address);
    }

    @Override
    @Transactional
    public AddressDTO update(String email, Long id, CreateAddressRequest request) {
        Address address = addressRepository.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada"));

        if (Boolean.TRUE.equals(request.isDefault()) && !address.getIsDefault()) {
            addressRepository.findByUser_EmailAndIsDefaultTrue(email)
                    .ifPresent(addr -> addr.setIsDefault(false));
        }

        address.setType(request.type());
        address.setFullName(request.fullName());
        address.setStreet(request.street());
        address.setApartment(request.apartment());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
        address.setCountry(request.country());
        address.setPhone(request.phone());
        address.setIsDefault(Boolean.TRUE.equals(request.isDefault()));

        Address saved = addressRepository.save(address);
        return addressMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(String email, Long id) {
        Address address = addressRepository.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada"));
        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public AddressDTO setAsDefault(String email, Long id) {
        Address address = addressRepository.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada"));

        addressRepository.findByUser_EmailAndIsDefaultTrue(email)
                .ifPresent(addr -> addr.setIsDefault(false));

        address.setIsDefault(true);
        Address saved = addressRepository.save(address);
        return addressMapper.toDto(saved);
    }
}
