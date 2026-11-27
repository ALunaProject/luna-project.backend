package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.gameCache.GameCacheCreate;
import com.luna.lunaproject.application.dto.gameList.GameListResponse;
import com.luna.lunaproject.application.services.FavoriteGamesServices;
import com.luna.lunaproject.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/favorites")
@RequiredArgsConstructor
public class FavoriteGamesController {
    private final FavoriteGamesServices favServices;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getAllFavorites(@PathVariable UUID userId) {
        GameListResponse res = favServices.getAllFavorites(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> addFavoriteGame(
            @RequestBody @Valid GameCacheCreate req,
            @AuthenticationPrincipal User currentUser
    ) {
        GameListResponse res = favServices.addFavGame(req, currentUser.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Object> deleteFavoriteGame(
            @PathVariable Integer appId,
            @AuthenticationPrincipal User currentUser
    ) {
        GameListResponse res = favServices.deleteFavGame(currentUser.getId(), appId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
