package com.luna.lunaproject.application.dto.gameCache;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameCacheResponse {
    Integer appid;
    String message;
}