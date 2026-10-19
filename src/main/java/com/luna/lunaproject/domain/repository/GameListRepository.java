package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.GameList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameListRepository extends JpaRepository<GameList, UUID> {
    List<GameList> findAllByUserId(UUID userId);
    Optional<GameList> findByIdAndUserId(UUID id, UUID userId);
    boolean existsByUserIdAndNameIgnoreCase(UUID userId, String name);
}
