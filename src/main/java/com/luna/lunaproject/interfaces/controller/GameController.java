package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.gameCache.GameCacheResponse;
import com.luna.lunaproject.application.services.GameCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameCacheService gameCacheService;

    @PostMapping("/{appid}")
    public ResponseEntity<Object> createGameCache(@PathVariable Integer appid) {
        GameCacheResponse res = gameCacheService.createGameCache(appid);
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/{appid}")
    public ResponseEntity<Object> getGameByAppid(@PathVariable Integer appid) {
        GameCacheResponse res = gameCacheService.getGameByAppid(appid);
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }
}
