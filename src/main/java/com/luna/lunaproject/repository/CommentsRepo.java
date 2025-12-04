package com.luna.lunaproject.repository;

import com.luna.lunaproject.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepo extends JpaRepository<Comments, Integer> {
    public String findByCommentId(int commentId);
}