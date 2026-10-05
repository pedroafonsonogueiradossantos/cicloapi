package com.pedro.clientesapi.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
        @NotBlank String name,
        String description
) {
}
