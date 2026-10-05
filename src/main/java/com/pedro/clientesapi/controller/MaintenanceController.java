package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.MaintenanceDueResponse;
import com.pedro.clientesapi.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceRepository maintenanceRepository;

    @GetMapping("/due")
    public List<MaintenanceDueResponse> due(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return maintenanceRepository.findDue(from, to).stream().map(MaintenanceDueResponse::from).toList();
    }
}
