package com.cabrejogym.platform_ecommerce.infrastructure.security.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String body = "{"
                + "\"timestamp\":\"" + Instant.now() + "\","
                + "\"status\":403,"
                + "\"error\":\"FORBIDDEN\","
                + "\"message\":\"Acceso denegado\","
                + "\"path\":\"" + request.getRequestURI() + "\","
                + "\"details\":null"
                + "}";

        response.getWriter().write(body);
    }
}
