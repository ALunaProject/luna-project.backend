package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.gameList.GameListCreate;
import com.luna.lunaproject.application.dto.gameList.GameListResponse;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.GameCache;
import com.luna.lunaproject.domain.entity.GameList;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.ListType;
import com.luna.lunaproject.domain.repository.GameListRepository;
import com.luna.lunaproject.domain.repository.GameRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameListServices {

    private final GameListRepository listRepo;
    private final UserRepository userRepo;
    private final GameRepository gameRepo;
    private final GameCacheService gameCacheService;

    @Transactional
    public GameListResponse createCustom(GameListCreate req, UUID userId) {

        if (listRepo.existsByUserIdAndNameIgnoreCase(userId, req.getName().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have a list with this name!");
        }

        User user = userRepo.getReferenceById(userId);
        GameList list = new GameList();
        list.setListType(ListType.CUSTOM);
        list.setName(req.getName().trim());
        list.setUser(user);

        GameList saved = listRepo.save(list);
        return new GameListResponse(saved);
    }

    @Transactional
    public GameListResponse addGameToList(UUID listId, Integer appId, UUID userId) throws AccessDeniedException {
        GameList list = listRepo.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("List not found: " + listId));

        if (!list.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can't modify this list");
        }

        GameCache game = gameCacheService.getOrCreateGameCacheEntity(appId);
        list.getGames().add(game);

        return new GameListResponse(listRepo.save(list));
    }

    @Transactional
    public String deleteList(UUID listId, UUID userId) throws AccessDeniedException {
        GameList list = listRepo.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can't modify this list");
        }
        if (list.isDefault()) {
            throw new BusinessRuleException("Default lists can't be deleted");
        }
        listRepo.delete(list);
        return "List deleted successfully!";
    }

    @Transactional
    public GameListResponse renameList(UUID listId, UUID userId, GameListCreate req) throws AccessDeniedException {
        GameList list = listRepo.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can't modify this list");
        }
        if (list.isDefault()) {
            throw new BusinessRuleException("Default lists can't be modified");
        }
        list.setName(req.getName());
        return new GameListResponse(listRepo.save(list));
    }

    @Transactional
    public GameListResponse deleteGameOnList(UUID listId, UUID userId, Integer appId) throws AccessDeniedException {
        GameList list = listRepo.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can't modify this list");
        }

        GameCache game = gameRepo.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found: " + appId));

        list.getGames().remove(game);

        return new GameListResponse(listRepo.save(list));
    }

    @Transactional
    public List<GameListResponse> getAllUserLists(UUID userId) {
        List<GameList> userLists = listRepo.findAllByUserId(userId);
        return userLists.stream().map(GameListResponse::new).toList();
    }


}
