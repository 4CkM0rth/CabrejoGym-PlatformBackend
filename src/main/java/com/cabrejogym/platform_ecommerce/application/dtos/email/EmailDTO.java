package com.cabrejogym.platform_ecommerce.application.dtos.email;

import java.util.Map;

public record EmailDTO(
        String to,
        String subject,
        String template,
        Map<String, Object> variables
) {}
