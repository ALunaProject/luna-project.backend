package com.luna.lunaproject.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank
    @Pattern(regexp = "\\S+", message = "O campo [username] não pode ter espaço")
    private String username;
    @Email(message = "O campo [email] deve conter um e-mail válido")
    private String email;
    // mesma regra do checklist do front (utils/passwordRules.ts): 8 a 100 caracteres, com minúscula, maiúscula, número e caractere especial
    @NotBlank(message = "A senha é obrigatória")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,100}$",
            message = "A senha deve ter de 8 a 100 caracteres, com letra maiúscula, minúscula, número e caractere especial."
    )
    private String password;
}
