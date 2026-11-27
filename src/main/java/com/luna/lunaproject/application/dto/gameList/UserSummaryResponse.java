package com.luna.lunaproject.application.dto.gameList;

import com.luna.lunaproject.domain.entity.User;

import java.util.UUID;

public record UserSummaryResponse(UUID id) {
    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(user.getId());
    }
}