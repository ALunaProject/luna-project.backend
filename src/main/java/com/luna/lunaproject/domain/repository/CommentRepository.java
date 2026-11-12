package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.Comment;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @EntityGraph(attributePaths = {"author", "post"})
    List<Comment> findByTargetTypeAndTargetIdOrderByCreationDateDesc(
            CommentTargetType type, String targetId);

    @EntityGraph(attributePaths = {"author", "post"})
    Optional<Comment> findFirstByAuthorIdAndTargetTypeAndTargetIdOrderByCreationDateDesc(
            UUID authorId, CommentTargetType targetType, String targetId);

    @EntityGraph(attributePaths = {"author", "post"})
    List<Comment> findByAuthorIdOrderByCreationDateDesc(UUID authorId);

    void deleteByTargetTypeAndTargetId(CommentTargetType type, String targetId);

    Optional<Comment> findFirstByAuthorIdOrderByCreationDateDesc(UUID authorId);

    boolean existsByAuthorIdAndTargetTypeAndTargetIdAndContentIgnoreCase(
            UUID authorId, CommentTargetType type, String targetId, String content);

    // Mantém comentários antigos de posts acessíveis enquanto linhas legadas ainda não têm target_type.
    @EntityGraph(attributePaths = {"author", "post"})
    List<Comment> findByPostIdAndTargetTypeIsNullOrderByCreationDateDesc(UUID postId);

    @EntityGraph(attributePaths = {"author", "post"})
    Optional<Comment> findFirstByAuthorIdAndPostIdAndTargetTypeIsNullOrderByCreationDateDesc(
            UUID authorId, UUID postId);

    boolean existsByAuthorIdAndPostIdAndTargetTypeIsNullAndContentIgnoreCase(
            UUID authorId, UUID postId, String content);

    void deleteByPostIdAndTargetTypeIsNull(UUID postId);
}
