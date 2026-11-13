package com.luna.lunaproject.application.dto.user;

import com.luna.lunaproject.domain.entity.User;
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
    private String bio;


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

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profilePicUrl = user.getProfilePicUrl();
        this.bannerUrl = user.getBannerUrl();
        this.bio = user.getBio();
        this.role = user.getRole() != null ? user.getRole().getRole() : null;
    }
}
