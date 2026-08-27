package com.luna.lunaproject.application.dto;

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
public class UserUpdateDto {

    @NotBlank
    @Pattern(regexp = "\\S+", message = "O campo [username] não pode ter espaço")
    private String username;
    private String profilePicUrl;
    private String bannerUrl;

}
