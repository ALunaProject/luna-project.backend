package com.luna.lunaproject.application.dto.gameCache;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameCacheCreate {
    @NotNull
    private Integer appid;

    @NotNull
    private Assets assets;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Assets {
        @NotBlank
        @JsonProperty("library_capsule")
        private String libraryCapsule;
    }
}