package com.luna.lunaproject.application.dto.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentRequestDTO {

    @NotBlank(message = "O conteúdo do comentário não pode estar vazio")
    @Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres")
    private String content;

    @NotNull(message = "O id do usuário é obrigatório")
    private UUID userId;
}