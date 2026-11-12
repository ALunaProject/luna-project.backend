package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.rating.RatingResponse;
import com.luna.lunaproject.domain.entity.Rating;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.RatingTargetType;
import com.luna.lunaproject.domain.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    @Transactional(readOnly = true)
    public Optional<RatingResponse> findUserRating(UUID userId, RatingTargetType type, String targetId) {
        return ratingRepository
                .findByUserIdAndTargetTypeAndTargetId(userId, type, targetId)
                .map(this::toResponse);
    }

    @Transactional
    public RatingResponse rate(User user, RatingTargetType type, String targetId, BigDecimal score) {
        Rating rating = ratingRepository
                .findByUserIdAndTargetTypeAndTargetId(user.getId(), type, targetId)
                .orElseGet(() -> Rating.builder()
                        .user(user)
                        .targetType(type)
                        .targetId(targetId)
                        .build());

        rating.setScore(score);
        return toResponse(ratingRepository.save(rating));
    }

    @Transactional
    public void remove(UUID userId, RatingTargetType type, String targetId) {
        ratingRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, type, targetId);
    }

    private RatingResponse toResponse(Rating r) {
        return RatingResponse.builder()
                .id(r.getId())
                .score(r.getScore())
                .targetType(r.getTargetType().name())
                .targetId(r.getTargetId())
                .creationDate(r.getCreationDate())
                .updatedDate(r.getUpdatedDate())
                .build();
    }
}