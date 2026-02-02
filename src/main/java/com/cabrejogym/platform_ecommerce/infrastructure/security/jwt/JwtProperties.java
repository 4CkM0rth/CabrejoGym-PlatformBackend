package com.cabrejogym.platform_ecommerce.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String issuer,
        long accessTokenMinutes,
        String secretBase64
) {
}
