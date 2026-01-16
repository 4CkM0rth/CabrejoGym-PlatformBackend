package com.cabrejogym.platform_ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/bcrypt")
    public String bcrypt(@RequestParam String raw) {
        return passwordEncoder.encode(raw);
    }
}
