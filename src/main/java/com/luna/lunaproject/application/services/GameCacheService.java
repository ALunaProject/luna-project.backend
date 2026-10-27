package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.gameCache.GameCacheRequest;
import com.luna.lunaproject.application.dto.gameCache.GameCacheResponse;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.GameCache;
import com.luna.lunaproject.domain.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameCacheService {
    private final GameRepository gameRepository;

    public GameCacheService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameCacheResponse createGameCache(GameCacheRequest req) {
        GameCache gameCache = getOrCreateGameCacheEntity(req);
        GameCacheResponse.Assets responseAssets = convertAssets(req.getAssets());
        if (gameCache != null) {
            return new GameCacheResponse(gameCache.getAppid(), "This game already exists in cache.", responseAssets);
        }

        GameCache newGame = new GameCache();
        newGame.setAppid(req.getAppid());
        GameCache savedGame = gameRepository.save(newGame);

        return new GameCacheResponse(savedGame.getAppid(), "Game added to cache!", responseAssets);
    }

    public GameCache getOrCreateGameCacheEntity(GameCacheRequest req) {
        return gameRepository.findById(req.getAppid())
                .map(existingGame -> {
                    if (req.getAssets() != null) {
                        existingGame.setLibraryCapsule(req.getAssets().getLibraryCapsule());
                    }
                    return gameRepository.save(existingGame);
                })
                .orElseGet(() -> {
                    // Se não existe cria um novo preenchendo tudo
                    GameCache newGame = new GameCache();
                    newGame.setAppid(req.getAppid());

                    if (req.getAssets() != null) {
                        newGame.setLibraryCapsule(req.getAssets().getLibraryCapsule());
                    }
                    return gameRepository.save(newGame);
                });
    }

    public GameCacheResponse getGameByAppid(GameCacheRequest req) {
        GameCache gameCache = gameRepository.findById(req.getAppid())
                .orElseThrow(() -> new ResourceNotFoundException("Game not found on cache"));
        GameCacheResponse.Assets responseAssets = convertAssets(req.getAssets());

        return new GameCacheResponse(req.getAppid(), "Game founded in cache.", responseAssets);
    }

    private GameCacheResponse.Assets convertAssets(GameCacheRequest.Assets requestAssets) {
        if (requestAssets == null) {
            return null;
        }

        GameCacheResponse.Assets responseAssets = new GameCacheResponse.Assets();
        responseAssets.setLibraryCapsule(requestAssets.getLibraryCapsule());

        return responseAssets;
    }
}
