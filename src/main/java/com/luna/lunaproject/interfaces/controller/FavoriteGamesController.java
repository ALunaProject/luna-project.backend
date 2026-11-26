package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.gameCache.GameCacheCreate;
import com.luna.lunaproject.application.dto.gameList.GameListResponse;
import com.luna.lunaproject.application.services.FavoriteGamesServices;
import com.luna.lunaproject.domain.entity.GameList;
import com.luna.lunaproject.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/favorites")
@RequiredArgsConstructor
public class FavoriteGamesController {
    private final FavoriteGamesServices favServices;

    @GetMapping()
    public ResponseEntity<Object> getAllFavorites(@AuthenticationPrincipal User currentUser) {
        GameListResponse res = favServices.getAllFavorites(currentUser.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> addFavotiteGame(
            @RequestBody @Valid GameCacheCreate req,
            @AuthenticationPrincipal User currentUser
    ) {
        GameListResponse res = favServices.addFavGame(req, currentUser.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteFavoriteGame(
            @RequestBody @Valid GameCacheCreate req,
            @AuthenticationPrincipal User currentUser
    ) {
        GameListResponse res = favServices.deleteFavGame(currentUser.getId(), req.getAppid());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
