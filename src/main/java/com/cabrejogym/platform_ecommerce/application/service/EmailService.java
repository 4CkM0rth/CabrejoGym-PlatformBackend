package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.email.EmailDTO;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import com.cabrejogym.platform_ecommerce.domain.entity.User;

public interface EmailService {
    void sendEmail(EmailDTO emailDTO);
    void sendWelcomeEmail(User user);
    void sendOrderConfirmationEmail(Order order);
    void sendPasswordResetEmail(User user, String resetToken);
}
