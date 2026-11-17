package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.GameList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameListRepository extends JpaRepository<GameList, UUID> {
    List<GameList> findAllByUserId(UUID userId);
    Optional<GameList> findByIdAndUserId(UUID id, UUID userId);
    boolean existsByUserIdAndNameIgnoreCase(UUID userId, String name);
}
