package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.GameCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameCache, Integer> {
    GameCache findByAppid(Integer appid);
}

