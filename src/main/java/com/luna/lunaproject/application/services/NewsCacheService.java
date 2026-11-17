package com.luna.lunaproject.application.services;

import com.luna.lunaproject.domain.entity.NewsCache;
import com.luna.lunaproject.domain.repository.NewsRepository;
import org.springframework.stereotype.Service;

@Service
public class NewsCacheService {
    private final NewsRepository newsRepository;

    public NewsCacheService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

//    public NewsCache createNewsCache(NewsCache req) {
//
//    }

    public NewsCache getOrCreateNewsCache(NewsCache req) {

        return newsRepository.findNewsByid(req.getId());
    }

}
