package com.luna.lunaproject.application.dto.gameCache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luna.lunaproject.domain.entity.GameCache;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameCacheResponse {
    Integer appid;
    String message;
    Assets assets;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Assets {
        @JsonProperty("library_capsule")
        String libraryCapsule;
    }
    public static GameCacheResponse from(GameCache game) {
        return GameCacheResponse.builder()
                .appid(game.getAppid())
                .assets(new Assets(game.getLibraryCapsule()))
                .build();
    }

}