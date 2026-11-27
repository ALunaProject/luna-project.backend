package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.rating.RatingRequest;
import com.luna.lunaproject.application.dto.rating.RatingResponse;
import com.luna.lunaproject.application.services.RatingService;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.RatingTargetType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings/games")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/{appId}/me")
    public ResponseEntity<RatingResponse> getMyRating(
            @PathVariable String appId,
            @AuthenticationPrincipal User user) {

        return ratingService.findUserRating(user.getId(), RatingTargetType.GAME, appId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{appId}")
    public ResponseEntity<RatingResponse> rateGame(
            @PathVariable String appId,
            @Valid @RequestBody RatingRequest body,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                ratingService.rate(user, RatingTargetType.GAME, appId, body.getScore()));
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> removeRating(
            @PathVariable String appId,
            @AuthenticationPrincipal User user) {

        ratingService.remove(user.getId(), RatingTargetType.GAME, appId);
        return ResponseEntity.noContent().build();
    }
}