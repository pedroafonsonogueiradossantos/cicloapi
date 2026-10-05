package com.pedro.clientesapi.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonRequest(
        @NotBlank String name
) {
}
