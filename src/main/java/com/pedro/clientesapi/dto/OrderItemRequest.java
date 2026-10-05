package com.pedro.clientesapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotNull Integer orderId,
        @NotNull Integer itemId,
        @NotNull @Positive Short quantity,
        BigDecimal unitPrice,
        @Positive Integer maintenanceIntervalMonths
) {
}
