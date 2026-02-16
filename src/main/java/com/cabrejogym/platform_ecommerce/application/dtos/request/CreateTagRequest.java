package com.cabrejogym.platform_ecommerce.application.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank
        String name
) {}
