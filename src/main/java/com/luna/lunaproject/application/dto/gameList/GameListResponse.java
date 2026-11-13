package com.luna.lunaproject.application.dto.gameList;

import com.luna.lunaproject.application.dto.gameCache.GameCacheResponse;
import com.luna.lunaproject.domain.entity.GameList;
import com.luna.lunaproject.domain.enums.ListType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameListResponse {
    private UUID id;
    private String name;
    private ListType type;
    private UserSummaryResponse user;
    private Set<GameCacheResponse> games;

    public GameListResponse(GameList list) {
        this.id = list.getId();
        this.name = list.getName();
        this.type = list.getListType();
        this.user = UserSummaryResponse.from(list.getUser());
        this.games = list.getGames().stream()
                .map(GameCacheResponse::from)
                .collect(Collectors.toSet());
    }
}
