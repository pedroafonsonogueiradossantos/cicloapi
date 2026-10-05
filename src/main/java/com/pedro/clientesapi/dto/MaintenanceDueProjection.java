package com.pedro.clientesapi.dto;

import java.time.Instant;

public interface MaintenanceDueProjection {
    Integer getPersonId();
    String getPersonName();
    Integer getItemId();
    String getItemName();
    Integer getLastOrderId();
    Instant getLastOrderDate();
    Integer getMaintenanceIntervalMonths();
    Instant getDueDate();
    String getPhoneNumbers();
}
