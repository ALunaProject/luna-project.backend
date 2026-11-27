package com.luna.lunaproject.application.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {
    private UUID id;
    private String description;
    private UUID userId;
    private String username;
    private String targetType;
    private String targetId;
    private LocalDateTime creationDate;
}