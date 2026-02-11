package com.cabrejogym.platform_ecommerce.infrastructure.repository;

import com.cabrejogym.platform_ecommerce.domain.entity.Refund;
import com.cabrejogym.platform_ecommerce.domain.enums.RefundStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    boolean existsByOrder_Id(Long orderId);

    List<Refund> findByUser_EmailOrderByRequestedAtDesc(String email);

    Optional<Refund> findByIdAndUser_Email(Long id, String email);

    boolean existsByOrder_IdAndStatus(Long orderId, RefundStatus status);

    Optional<Refund> findByOrder_Id(Long orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT r
       FROM Refund r
       JOIN FETCH r.order o
       WHERE r.id = :id
       """)
    Optional<Refund> findByIdWithOrderForUpdate(@Param("id") Long id);

}
