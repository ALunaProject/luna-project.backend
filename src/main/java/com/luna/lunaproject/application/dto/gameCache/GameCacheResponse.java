package com.luna.lunaproject.application.dto.gameCache;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}