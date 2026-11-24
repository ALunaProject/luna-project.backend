package com.luna.lunaproject.application.services;

import com.luna.lunaproject.domain.entity.NewsCache;
import com.luna.lunaproject.domain.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsCacheService {
    private final NewsRepository newsRepository;

    public NewsCacheService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public NewsCache createNewsCache(NewsCache req) {
        if (req == null || req.getId() == null || req.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Request or News ID cannot be null or empty");
        }

        Optional<NewsCache> existingNews = newsRepository.findById(Integer.valueOf(req.getId()));

        if (existingNews.isPresent()) {
            return new NewsCache(req.getId(), "This news already exists in cache");
        }

        NewsCache newNews = new NewsCache();
        newNews.setId(req.getId());

        NewsCache savedNews = newsRepository.save(newNews);

        return new NewsCache(savedNews.getId(), "News saved in cache");
    }
}
