package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // Lista os comentários de um post, do mais antigo para o mais novo
    List<Comment> findByPostIdOrderByCreationDateAsc(UUID postId);

    // Lista os comentários feitos por um usuário específico
    List<Comment> findByUserIdOrderByCreationDateDesc(UUID userId);
}