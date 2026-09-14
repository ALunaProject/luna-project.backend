package com.luna.lunaproject.application.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String conteudo;
    private Long autorId;
    private String autorNome;
    private Long postId;
    private LocalDateTime dataCriacao;
}