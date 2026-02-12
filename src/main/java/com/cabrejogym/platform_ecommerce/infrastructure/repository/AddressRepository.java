package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Address;
import com.cabrejogym.platform_ecommerce.domain.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByUser_Email(String email);
    
    Optional<Address> findByIdAndUser_Email(Long id, String email);
    
    Optional<Address> findByUser_EmailAndIsDefaultTrue(String email);
    
    List<Address> findByUser_EmailAndType(String email, AddressType type);
}
