package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Lista os comentários de um post, do mais antigo para o mais novo
    List<Comment> findByPostIdOrderByDataCriacaoAsc(Long postId);

    // Lista os comentários feitos por um usuário específico
    List<Comment> findByAutorIdOrderByDataCriacaoDesc(Long autorId);
}