package com.luna.lunaproject.repository;

import com.luna.lunaproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    public Post findByTitle(String title);
}
