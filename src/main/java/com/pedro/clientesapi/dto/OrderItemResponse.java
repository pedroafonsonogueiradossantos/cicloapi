package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Integer id,
        Integer orderId,
        Integer itemId,
        String itemName,
        Short quantity,
        BigDecimal unitPrice,
        Integer maintenanceIntervalMonths
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getItem().getId(),
                orderItem.getItem().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getMaintenanceIntervalMonths()
        );
    }
}
