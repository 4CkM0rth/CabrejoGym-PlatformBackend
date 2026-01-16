package com.cabrejogym.platform_ecommerce.config;

import com.cabrejogym.platform_ecommerce.security.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
}
