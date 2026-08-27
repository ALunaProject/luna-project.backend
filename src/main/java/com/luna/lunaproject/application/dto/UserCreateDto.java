package com.luna.lunaproject.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 6, max = 100, message = "A senha deve ter entre 6 a 100 caracteres.")
    private String password;
}
