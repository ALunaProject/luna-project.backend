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

    private Long id;
    private String conteudo;
    private UUID autorId;
    private String autorNome;
    private UUID postId;
    private LocalDateTime dataCriacao;
}