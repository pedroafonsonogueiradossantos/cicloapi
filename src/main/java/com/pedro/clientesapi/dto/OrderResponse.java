package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.CustomerOrder;

import java.time.OffsetDateTime;

public record OrderResponse(
        Integer id,
        Integer personId,
        String personName,
        String observation,
        OffsetDateTime orderDate,
        OffsetDateTime createdAt,
        String addressStreet,
        String addressNumber,
        String addressNeighborhood,
        String addressCityName,
        Integer originAddressId
) {
    public static OrderResponse from(CustomerOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getPerson().getId(),
                order.getPerson().getName(),
                order.getObservation(),
                order.getOrderDate(),
                order.getCreatedAt(),
                order.getAddressStreet(),
                order.getAddressNumber(),
                order.getAddressNeighborhood(),
                order.getAddressCityName(),
                order.getOriginAddress() != null ? order.getOriginAddress().getId() : null
        );
    }
}
