package com.luna.lunaproject.application.dto.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.luna.lunaproject.domain.enums.CommentTargetType;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    @NotBlank(message = "O conteúdo do comentário não pode estar vazio")
    @Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres")
    private String content;

    private UUID postId;

    private CommentTargetType targetType;

    private String targetId;
}
