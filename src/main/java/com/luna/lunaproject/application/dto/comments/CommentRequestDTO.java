package com.luna.lunaproject.application.dto.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    @NotBlank(message = "O conteúdo do comentário não pode estar vazio")
    @Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres")
    private String content;
}
