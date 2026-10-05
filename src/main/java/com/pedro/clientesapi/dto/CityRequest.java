package com.pedro.clientesapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CityRequest(
        @NotBlank String name
) {
}
