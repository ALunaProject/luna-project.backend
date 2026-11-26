package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.NewsCache;
import com.luna.lunaproject.domain.repository.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class NewsCacheService {
    private final NewsRepository newsRepository;

    public NewsCacheService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Transactional
    public NewsCache getOrCreateNewsCache(String newsId) {
        if (newsId == null || newsId.isBlank()) {
            throw new IllegalArgumentException("News ID cannot be null or empty");
        }
        return newsRepository.findById(newsId)
                .orElseGet(() -> newsRepository.save(new NewsCache(newsId)));
    }

    @Transactional(readOnly = true)
    public NewsCache getNewsById(String newsId) {
        if (newsId == null || newsId.isBlank()) {
            throw new IllegalArgumentException("News ID cannot be null or empty");
        }
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + newsId));
    }
}
