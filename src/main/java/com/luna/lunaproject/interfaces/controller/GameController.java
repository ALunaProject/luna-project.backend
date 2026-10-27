package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.gameCache.GameCacheRequest;
import com.luna.lunaproject.application.dto.gameCache.GameCacheResponse;
import com.luna.lunaproject.application.services.GameCacheService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameCacheService gameCacheService;

    @PostMapping()
    public ResponseEntity<Object> createGameCache(@RequestBody @Valid GameCacheRequest req) {
        GameCacheResponse res = gameCacheService.createGameCache(req);
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Object> getGameByAppid(GameCacheRequest req) {
        GameCacheResponse res = gameCacheService.getGameByAppid(req);
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }
}
