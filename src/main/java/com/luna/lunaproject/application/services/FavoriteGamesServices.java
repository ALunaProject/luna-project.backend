package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.gameCache.GameCacheCreate;
import com.luna.lunaproject.application.dto.gameList.GameListResponse;
import com.luna.lunaproject.application.exceptions.BusinessRuleException;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.GameCache;
import com.luna.lunaproject.domain.entity.GameList;
import com.luna.lunaproject.domain.enums.ListType;
import com.luna.lunaproject.domain.repository.GameListRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteGamesServices {
    private static final int MAX_FAVORITES = 4;
    private final GameListRepository listRepo;
    private final UserRepository userRepo;
    private final GameCacheService gameCacheService;


    @Transactional(readOnly = true)
    public GameListResponse getAllFavorites(UUID userId) {
        GameList favorites = listRepo.findByUserIdAndListType(userId, ListType.FAVORITES)
                .orElseThrow(() -> new ResourceNotFoundException("Favorites list not found"));

        return new GameListResponse(favorites);
    }

    @Transactional
    public GameListResponse addFavGame(GameCacheCreate req, UUID userId) {
        GameList favorites = getOrCreateFavoritesList(userId);

        boolean alreadyFavorite = favorites.getGames().stream()
                .anyMatch(g -> g.getAppid().equals(req.getAppid()));
        if (alreadyFavorite) {
            throw new BusinessRuleException("This game is already in your favorites");
        }

        if (favorites.getGames().size() >= MAX_FAVORITES) {
            throw new BusinessRuleException("You can only have up to " + MAX_FAVORITES + " favorite games");
        }

        GameCache game = gameCacheService.getOrCreateGameCacheEntity(req);
        favorites.getGames().add(game);

        return new GameListResponse(listRepo.save(favorites));
    }

    @Transactional
    public GameListResponse deleteFavGame(UUID userId, Integer appId) {
        GameList favorites = listRepo.findByUserIdAndListType(userId, ListType.FAVORITES)
                .orElseThrow(() -> new ResourceNotFoundException("Favorites list not found"));

        boolean removed = favorites.getGames().removeIf(g -> g.getAppid().equals(appId));
        if (!removed) {
            throw new ResourceNotFoundException("Game not found in favorites: " + appId);
        }

        return new GameListResponse(listRepo.save(favorites));
    }

    // ---------- helpers ----------

    private GameList getOrCreateFavoritesList(UUID userId) {
        return listRepo.findByUserIdAndListType(userId, ListType.FAVORITES)
                .orElseGet(() -> {
                    GameList list = new GameList();
                    list.setListType(ListType.FAVORITES);
                    list.setName("Favorites");
                    list.setUser(userRepo.getReferenceById(userId));
                    return listRepo.save(list);
                });
    }
}