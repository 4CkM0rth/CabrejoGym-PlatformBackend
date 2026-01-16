package com.cabrejogym.platform_ecommerce.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String issuer,
        long accessTokenMinutes,
        String secretBase64
) {
}
