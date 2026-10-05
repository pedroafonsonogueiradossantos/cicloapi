package com.pedro.clientesapi.dto;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotNull Integer personId,
        String street,
        String number,
        String neighborhood,
        Integer cityId
) {
}
