package com.pedro.clientesapi.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record OrderRequest(
        @NotNull Integer personId,
        Integer addressId,
        String observation,
        OffsetDateTime orderDate
) {
}
