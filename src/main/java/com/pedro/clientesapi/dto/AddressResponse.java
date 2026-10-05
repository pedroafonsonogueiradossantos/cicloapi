package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.Address;

import java.time.OffsetDateTime;

public record AddressResponse(
        Integer id,
        Integer personId,
        String street,
        String number,
        String neighborhood,
        Integer cityId,
        String cityName,
        OffsetDateTime createdAt
) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getPerson().getId(),
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity() != null ? address.getCity().getId() : null,
                address.getCity() != null ? address.getCity().getName() : null,
                address.getCreatedAt()
        );
    }
}
