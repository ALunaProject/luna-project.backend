package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.services.NewsCacheService;
import com.luna.lunaproject.domain.entity.NewsCache;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsCacheService newsCacheService;

    @PostMapping
    public ResponseEntity<NewsCache> createNewsCache(@RequestBody NewsCache req) {
        NewsCache res = newsCacheService.getOrCreateNewsCache(req.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<NewsCache> getNewsById(@RequestParam String id) {
        NewsCache res = newsCacheService.getNewsById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}