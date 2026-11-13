package com.luna.lunaproject.application.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private UUID id;
    private BigDecimal score;
    private String targetType;
    private String targetId;
    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;
}