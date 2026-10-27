package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.gameCache.GameCacheRequest;
import com.luna.lunaproject.application.dto.gameList.GameListCreate;
import com.luna.lunaproject.application.dto.gameList.GameListResponse;
import com.luna.lunaproject.application.services.GameListServices;
import com.luna.lunaproject.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/lists")
@RequiredArgsConstructor
public class GameListController {
    private final GameListServices listServices;

    @PostMapping
    private ResponseEntity<Object> createCustomList(
            @Valid @RequestBody GameListCreate req,
            @AuthenticationPrincipal User currentUserId) {
        GameListResponse res = listServices.createCustom(req, currentUserId.getId());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/{listId}/games")
    private ResponseEntity<Object> addGamesToCustomList(
            @PathVariable UUID listId,
            @RequestBody @Valid GameCacheRequest req,
            @AuthenticationPrincipal User currentUser)
            throws AccessDeniedException {
        GameListResponse res = listServices.addGameToList(listId, req, currentUser.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{listId}/games")
    private ResponseEntity<Object> removeGameOnList(
            @PathVariable UUID listId,
            @RequestBody @Valid GameCacheRequest req,
            @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        GameListResponse res = listServices.deleteGameOnList(listId, currentUser.getId(), req);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{listId}")
    private ResponseEntity<Object> deleteList(
            @PathVariable UUID listId,
            @AuthenticationPrincipal User currentUser) throws AccessDeniedException {
        String res = listServices.deleteList(listId, currentUser.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{listId}")
    private ResponseEntity<Object> renameList(
            @PathVariable UUID listId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody GameListCreate req
    ) throws AccessDeniedException {
        GameListResponse res = listServices.renameList(listId, currentUser.getId(), req);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{currentUser}")
    private ResponseEntity<Object> getAllListByUser(
            @PathVariable User currentUser
    ) {
        List<GameListResponse> res = listServices.getAllUserLists(currentUser.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
