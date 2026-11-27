package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.reports.ReportRequestDTO;
import com.luna.lunaproject.application.dto.reports.ReportResponseDTO;
import com.luna.lunaproject.application.services.ReportService;
import com.luna.lunaproject.domain.enums.ReportTargetType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{type}/{targetId}")
    public ResponseEntity<ReportResponseDTO> create(
            @PathVariable ReportTargetType type,
            @PathVariable String targetId,
            @Valid @RequestBody ReportRequestDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.create(type, targetId, body));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAll() {
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.delete(id));
    }
}