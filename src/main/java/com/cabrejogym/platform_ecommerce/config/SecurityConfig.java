package com.cabrejogym.platform_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Para APIs REST: si no usas sesión/cookies, normalmente deshabilitas CSRF
                .csrf(csrf -> csrf.disable())

                // Reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitimos el registro sin login
                        .requestMatchers("/api/usuarios/**").permitAll()
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )

                // Método de auth simple para pruebas (Postman puede usar Basic Auth)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
