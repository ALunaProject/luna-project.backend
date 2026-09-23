package com.luna.lunaproject.application.dto.comments;

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
public class CommentResponseDTO {

    private UUID id;
    private String content;
    private UUID userId;
    private String username;
    private UUID postId;
    private LocalDateTime creationDate;
}