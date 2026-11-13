package com.luna.lunaproject.application.dto.post;

import com.luna.lunaproject.domain.entity.User;

import java.util.UUID;

public record PostAuthorDto(UUID id, String username, String profilePicUrl) {

    public static PostAuthorDto from(User user) {
        if (user == null) return null;
        return new PostAuthorDto(user.getId(), user.getUsername(), user.getProfilePicUrl());
    }
}