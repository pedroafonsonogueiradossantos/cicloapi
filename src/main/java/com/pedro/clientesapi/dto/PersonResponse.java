package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.Person;

import java.time.OffsetDateTime;

public record PersonResponse(
        Integer id,
        String name,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static PersonResponse from(Person person) {
        return new PersonResponse(person.getId(), person.getName(), person.getCreatedAt(), person.getUpdatedAt());
    }
}
