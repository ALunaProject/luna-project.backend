package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.NewsCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsCache, String> {
}
