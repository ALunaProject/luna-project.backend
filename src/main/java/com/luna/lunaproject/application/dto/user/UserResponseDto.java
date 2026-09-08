package com.luna.lunaproject.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {

    private UUID id;
    private String username;
    private String email;
    private String profilePicUrl;
    private String bannerUrl;
    private String role;


    public UserResponseDto(UUID id, String username, String email, String role) {
        this.id =  id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public UserResponseDto(UUID id, String username) {
        this.id = id;
        this.username = username;
    }
}
