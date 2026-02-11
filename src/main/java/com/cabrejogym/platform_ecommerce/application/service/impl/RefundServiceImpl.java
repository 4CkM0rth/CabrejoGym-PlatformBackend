package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AdminRefundDecisionRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateRefundRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.RefundDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.refund.RefundMapper;
import com.cabrejogym.platform_ecommerce.application.rule.OrderStatusTransitionValidator;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import com.cabrejogym.platform_ecommerce.domain.entity.Refund;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.domain.enums.OrderStatus;
import com.cabrejogym.platform_ecommerce.domain.enums.RefundStatus;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ForbiddenException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.OrderRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.RefundRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import com.cabrejogym.platform_ecommerce.application.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RefundMapper refundMapper;

    @Override
    @Transactional
    public RefundDTO createMyRefund(String email, CreateRefundRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + request.orderId()));

        if (!order.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("No tienes permiso para solicitar devolución de esta orden");
        }

        if (order.getStatus() != OrderStatus.PAID) {
            throw new ConflictException("Solo puedes solicitar devolución para órdenes pagadas");
        }

        if (refundRepository.existsByOrder_IdAndStatus(order.getId(), RefundStatus.REQUESTED)
                || refundRepository.existsByOrder_IdAndStatus(order.getId(), RefundStatus.APPROVED)) {
            throw new ConflictException("Ya existe una devolución en proceso para esta orden");
        }

        if (refundRepository.existsByOrder_IdAndStatus(order.getId(), RefundStatus.REFUNDED)) {
            throw new ConflictException("Esta orden ya fue reembolsada");
        }

        OrderStatusTransitionValidator.validate(order.getStatus(), OrderStatus.RETURN_REQUESTED);
        order.setStatus(OrderStatus.RETURN_REQUESTED);

        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setUser(user);
        refund.setStatus(RefundStatus.REQUESTED);
        refund.setAmount(order.getTotal());
        refund.setReason(request.reason());
        refund.setRequestedAt(Instant.now());

        Refund saved = refundRepository.save(refund);
        return refundMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDTO> listMyRefunds(String email) {
        return refundRepository.findByUser_EmailOrderByRequestedAtDesc(email)
                .stream()
                .map(refundMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RefundDTO getMyRefundById(String email, Long refundId) {
        Refund refund = refundRepository.findByIdAndUser_Email(refundId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Devolución no encontrada con id: " + refundId));
        return refundMapper.toDto(refund);
    }

    @Override
    @Transactional
    public RefundDTO approve(Long refundId, AdminRefundDecisionRequest request) {

        Refund refund = refundRepository.findByIdWithOrderForUpdate(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Devolución no encontrada con id: " + refundId));

        if (refund.getStatus() != RefundStatus.REQUESTED) {
            throw new ConflictException("Solo se puede aprobar una devolución en estado REQUESTED");
        }

        Order order = refund.getOrder();
        if (order == null) {
            throw new ConflictException("La devolución no tiene una orden asociada");
        }

        if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {
            throw new ConflictException("La orden debe estar en RETURN_REQUESTED para aprobar la devolución");
        }

        refund.setStatus(RefundStatus.APPROVED);
        refund.setAdminNote(request.adminNote());
        refund.setResolvedAt(Instant.now());

        return refundMapper.toDto(refundRepository.save(refund));
    }

    @Override
    @Transactional
    public RefundDTO reject(Long refundId, AdminRefundDecisionRequest request) {

        Refund refund = refundRepository.findByIdWithOrderForUpdate(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Devolución no encontrada con id: " + refundId));

        if (refund.getStatus() != RefundStatus.REQUESTED) {
            throw new ConflictException("Solo se puede rechazar una devolución en estado REQUESTED");
        }

        Order order = refund.getOrder();
        if (order == null) {
            throw new ConflictException("La devolución no tiene una orden asociada");
        }

        if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {
            throw new ConflictException("La orden debe estar en RETURN_REQUESTED para rechazar la devolución");
        }

        refund.setStatus(RefundStatus.REJECTED);
        refund.setAdminNote(request.adminNote());
        refund.setResolvedAt(Instant.now());

        return refundMapper.toDto(refundRepository.save(refund));
    }

    @Override
    @Transactional
    public RefundDTO markAsRefunded(Long refundId, AdminRefundDecisionRequest request) {

        Refund refund = refundRepository.findByIdWithOrderForUpdate(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Devolución no encontrada con id: " + refundId));

        if (refund.getStatus() != RefundStatus.APPROVED) {
            throw new ConflictException("Solo se puede marcar como reembolsada una devolución en estado APPROVED");
        }

        Order order = refund.getOrder();
        if (order == null) {
            throw new ConflictException("La devolución no tiene una orden asociada");
        }

        if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {
            throw new ConflictException("La orden debe estar en RETURN_REQUESTED para marcar como reembolsada");
        }

        refund.setStatus(RefundStatus.REFUNDED);
        refund.setAdminNote(request.adminNote());
        refund.setResolvedAt(Instant.now());

        order.setStatus(OrderStatus.REFUNDED);

        Refund savedRefund = refundRepository.save(refund);
        orderRepository.save(order);

        return refundMapper.toDto(savedRefund);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RefundDTO> listAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        return refundRepository.findAll(
                        PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "requestedAt"))
                )
                .map(refundMapper::toDto);
    }
}