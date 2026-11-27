package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.Rating;
import com.luna.lunaproject.domain.enums.RatingTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    Optional<Rating> findByUserIdAndTargetTypeAndTargetId(UUID userId, RatingTargetType targetType, String targetId);

    void deleteByUserIdAndTargetTypeAndTargetId(UUID userId, RatingTargetType targetType, String targetId);
}