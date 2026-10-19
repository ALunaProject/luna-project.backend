package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.gameCache.GameCacheResponse;
import com.luna.lunaproject.domain.entity.GameCache;
import com.luna.lunaproject.domain.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameCacheService {
    private final GameRepository gameRepository;

    public GameCacheService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameCacheResponse createGameCache(Integer appid) {
        GameCache gameCache = gameRepository.findByAppid(appid);

        if (gameCache != null) {
            return new GameCacheResponse(gameCache.getAppid(), "This game already exists in cache.");
        }

        GameCache newGame = new GameCache();
        newGame.setAppid(appid);
        GameCache savedGame = gameRepository.save(newGame);

        return new GameCacheResponse(savedGame.getAppid(), "Game added to cache!");
    }

    public GameCacheResponse getGameByAppid(Integer appid) {
        GameCache gameCache = gameRepository.findById(appid)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found on cache"));

        return new GameCacheResponse(appid, "Game founded in cache.");
    }
}
