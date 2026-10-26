package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.Comment;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @EntityGraph(attributePaths = "author")   // evita N+1 ao montar o username
    List<Comment> findByTargetTypeAndTargetIdOrderByCreationDateDesc(CommentTargetType type, String targetId);

    @EntityGraph(attributePaths = "author")
    List<Comment> findByAuthorIdOrderByCreationDateDesc(UUID authorId);

    void deleteByTargetTypeAndTargetId(CommentTargetType type, String targetId);
}