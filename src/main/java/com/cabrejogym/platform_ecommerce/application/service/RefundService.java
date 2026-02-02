package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AdminRefundDecisionRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.CreateRefundRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.RefundDTO;

import java.util.List;

public interface RefundService {

    RefundDTO createMyRefund(String email, CreateRefundRequest request);

    List<RefundDTO> listMyRefunds(String email);

    RefundDTO getMyRefundById(String email, Long refundId);

    // Admin
    List<RefundDTO> listAll();

    RefundDTO approve(Long refundId, AdminRefundDecisionRequest request);

    RefundDTO reject(Long refundId, AdminRefundDecisionRequest request);

    RefundDTO markAsRefunded(Long refundId, AdminRefundDecisionRequest request);

}
