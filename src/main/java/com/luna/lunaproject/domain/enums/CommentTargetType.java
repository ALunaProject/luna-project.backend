package com.luna.lunaproject.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CommentTargetType {
    USER_PROFILE("profile"),
    POST("post"),
    GAME("game"),
    NEWS("news");

    private final String slug; // usado na URL

    public static CommentTargetType fromSlug(String value) {
        return Arrays.stream(values())
                .filter(t -> t.slug.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de alvo inválido: " + value));
    }
}

