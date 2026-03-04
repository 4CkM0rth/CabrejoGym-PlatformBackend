package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.email.EmailDTO;
import com.cabrejogym.platform_ecommerce.application.service.EmailService;
import com.cabrejogym.platform_ecommerce.domain.entity.Order;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    @Async
    public void sendEmail(EmailDTO emailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            context.setVariables(emailDTO.variables());
            String html = templateEngine.process(emailDTO.template(), context);

            helper.setTo(emailDTO.to());
            helper.setFrom(fromEmail);
            helper.setSubject(emailDTO.subject());
            helper.setText(html, true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", emailDTO.to());
        } catch (MessagingException e) {
            log.error("Error sending email to: {}", emailDTO.to(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(User user) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName());
        variables.put("userEmail", user.getEmail());
        variables.put("frontendUrl", frontendUrl);

        EmailDTO emailDTO = new EmailDTO(
                user.getEmail(),
                "¡Bienvenido a CabrejoGym!",
                "welcome-email",
                variables
        );

        sendEmail(emailDTO);
    }

    @Override
    public void sendOrderConfirmationEmail(Order order) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", order.getUser().getFirstName());
        variables.put("orderNumber", order.getOrderNumber());
        variables.put("orderTotal", order.getTotal());
        variables.put("orderItems", order.getItems());
        variables.put("frontendUrl", frontendUrl);
        variables.put("orderDate", order.getCreatedAt());

        EmailDTO emailDTO = new EmailDTO(
                order.getUser().getEmail(),
                "Confirmación de Orden #" + order.getOrderNumber(),
                "order-confirmation",
                variables
        );

        sendEmail(emailDTO);
    }

    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName());
        variables.put("resetLink", frontendUrl + "/reset-password?token=" + resetToken);
        variables.put("frontendUrl", frontendUrl);

        EmailDTO emailDTO = new EmailDTO(
                user.getEmail(),
                "Recuperación de Contraseña - CabrejoGym",
                "password-reset",
                variables
        );

        sendEmail(emailDTO);
    }
}
