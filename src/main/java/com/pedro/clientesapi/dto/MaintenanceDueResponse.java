package com.pedro.clientesapi.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record MaintenanceDueResponse(
        Integer personId,
        String personName,
        Integer itemId,
        String itemName,
        Integer lastOrderId,
        OffsetDateTime lastOrderDate,
        Integer maintenanceIntervalMonths,
        OffsetDateTime dueDate,
        List<String> phoneNumbers
) {
    public static MaintenanceDueResponse from(MaintenanceDueProjection p) {
        List<String> phones = (p.getPhoneNumbers() == null || p.getPhoneNumbers().isBlank())
                ? List.of()
                : List.of(p.getPhoneNumbers().split(",\\s*"));
        return new MaintenanceDueResponse(
                p.getPersonId(),
                p.getPersonName(),
                p.getItemId(),
                p.getItemName(),
                p.getLastOrderId(),
                p.getLastOrderDate().atOffset(java.time.ZoneOffset.UTC),
                p.getMaintenanceIntervalMonths(),
                p.getDueDate().atOffset(java.time.ZoneOffset.UTC),
                phones
        );
    }
}
