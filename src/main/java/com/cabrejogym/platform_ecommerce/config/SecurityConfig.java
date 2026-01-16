package com.cabrejogym.platform_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                            .requestMatchers("/api/me/**").authenticated()

                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
