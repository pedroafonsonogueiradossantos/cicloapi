package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.Item;

import java.time.OffsetDateTime;

public record ItemResponse(
        Integer id,
        String name,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getDescription(), item.getCreatedAt(), item.getUpdatedAt());
    }
}
