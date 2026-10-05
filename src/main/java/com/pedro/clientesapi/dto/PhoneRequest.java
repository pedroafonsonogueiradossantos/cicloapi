package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.PhoneType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PhoneRequest(
        @NotNull Integer personId,
        @NotBlank String number,
        PhoneType type
) {
}
