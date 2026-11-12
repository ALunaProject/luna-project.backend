package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.comments.CommentRequestDTO;
import com.luna.lunaproject.application.dto.comments.CommentResponseDTO;
import com.luna.lunaproject.application.dto.comments.CommentTargetResolver;
import com.luna.lunaproject.application.dto.gameCache.GameCacheCreate;
import com.luna.lunaproject.application.exceptions.ForbiddenOperationException;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.Comment;
import com.luna.lunaproject.domain.entity.Post;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import com.luna.lunaproject.domain.enums.UserRole;
import com.luna.lunaproject.domain.repository.CommentRepository;
import com.luna.lunaproject.domain.repository.PostRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final Duration COMMENT_COOLDOWN = Duration.ofMinutes(2);

    private final CommentRepository commentRepository;
    private final CommentTargetResolver targetResolver;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GameCacheService gameCacheService;

    @Transactional
    public CommentResponseDTO create(CommentTargetType type, String rawTargetId, CommentRequestDTO dto) {
        String targetId = targetResolver.resolve(type, rawTargetId);
        User author = getAuthenticatedUser();
        String content = dto.getContent().trim();

        checkCooldown(author, type, targetId);
        checkDuplicate(author, type, targetId, content);

        Post post = null;
        if (type == CommentTargetType.POST) {
            post = postRepository.findById(UUID.fromString(targetId))
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + targetId));
        } else if (type == CommentTargetType.GAME) {
            GameCacheCreate request = new GameCacheCreate();
            request.setAppid(Integer.valueOf(targetId));
            gameCacheService.getOrCreateGameCacheEntity(request);
        }

        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .post(post)
                .targetType(type)
                .targetId(targetId)
                .build();

        return toResponseDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> listByTarget(CommentTargetType type, String rawTargetId) {
        String targetId = targetResolver.resolve(type, rawTargetId);
        List<Comment> comments = new ArrayList<>(
                commentRepository.findByTargetTypeAndTargetIdOrderByCreationDateDesc(type, targetId));

        // Comentários criados antes do campo de destino são mantidos como comentários de post.
        if (type == CommentTargetType.POST) {
            comments.addAll(commentRepository.findByPostIdAndTargetTypeIsNullOrderByCreationDateDesc(
                    UUID.fromString(targetId)));
            comments.sort(Comparator.comparing(
                    Comment::getCreationDate,
                    Comparator.nullsLast(Comparator.reverseOrder())));
        }

        return comments.stream().map(this::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByPost(UUID postId) {
        return listByTarget(CommentTargetType.POST, postId.toString());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return commentRepository.findByAuthorIdOrderByCreationDateDesc(userId)
                .stream().map(this::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public CommentResponseDTO getCommentById(UUID commentId) {
        return toResponseDto(findOrThrow(commentId));
    }

    @Transactional
    public CommentResponseDTO update(UUID commentId, CommentRequestDTO dto) {
        Comment comment = findOrThrow(commentId);
        validateCanEdit(comment);
        comment.setContent(dto.getContent().trim());
        return toResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public String delete(UUID commentId) {
        Comment comment = findOrThrow(commentId);
        validateCanDelete(comment);
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }

    private Comment findOrThrow(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    private void checkCooldown(User author, CommentTargetType type, String targetId) {
        Comment last = commentRepository
                .findFirstByAuthorIdAndTargetTypeAndTargetIdOrderByCreationDateDesc(
                        author.getId(), type, targetId)
                .orElse(null);

        if (type == CommentTargetType.POST) {
            Comment legacyLast = commentRepository
                    .findFirstByAuthorIdAndPostIdAndTargetTypeIsNullOrderByCreationDateDesc(
                            author.getId(), UUID.fromString(targetId))
                    .orElse(null);
            if (legacyLast != null && (last == null
                    || legacyLast.getCreationDate().isAfter(last.getCreationDate()))) {
                last = legacyLast;
            }
        }

        if (last == null || last.getCreationDate() == null) return;

        LocalDateTime availableAt = last.getCreationDate().plus(COMMENT_COOLDOWN);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(availableAt)) {
            long seconds = Math.max(1, Duration.between(now, availableAt).toSeconds());
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Aguarde " + seconds + " segundos para comentar novamente");
        }
    }

    private void checkDuplicate(User author, CommentTargetType type, String targetId, String content) {
        boolean duplicated = commentRepository
                .existsByAuthorIdAndTargetTypeAndTargetIdAndContentIgnoreCase(
                        author.getId(), type, targetId, content);

        if (!duplicated && type == CommentTargetType.POST) {
            duplicated = commentRepository.existsByAuthorIdAndPostIdAndTargetTypeIsNullAndContentIgnoreCase(
                    author.getId(), UUID.fromString(targetId), content);
        }

        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Você já fez um comentário idêntico aqui");
        }
    }

    private void validateCanEdit(Comment comment) {
        User auth = getAuthenticatedUser();
        boolean isAuthor = comment.getAuthor().getId().equals(auth.getId());
        boolean isAdmin = auth.getRole() == UserRole.ADMIN;
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenOperationException("Você não tem permissão para alterar este comentário");
        }
    }

    private void validateCanDelete(Comment comment) {
        User auth = getAuthenticatedUser();
        boolean isAuthor = comment.getAuthor().getId().equals(auth.getId());
        boolean isAdmin = auth.getRole() == UserRole.ADMIN;
        boolean isProfileOwner = targetTypeOf(comment) == CommentTargetType.USER_PROFILE
                && targetIdOf(comment).equals(auth.getId().toString());
        if (!isAuthor && !isAdmin && !isProfileOwner) {
            throw new ForbiddenOperationException("Você não tem permissão para apagar este comentário");
        }
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private CommentTargetType targetTypeOf(Comment comment) {
        return comment.getTargetType() != null
                ? comment.getTargetType()
                : comment.getPost() != null ? CommentTargetType.POST : null;
    }

    private String targetIdOf(Comment comment) {
        if (comment.getTargetId() != null) return comment.getTargetId();
        return comment.getPost() != null ? comment.getPost().getId().toString() : "";
    }

    private CommentResponseDTO toResponseDto(Comment comment) {
        CommentTargetType targetType = targetTypeOf(comment);
        String targetId = targetIdOf(comment);
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getAuthor().getId())
                .username(comment.getAuthor().getUsername())
                .profilePicUrl(comment.getAuthor().getProfilePicUrl())
                .postId(comment.getPost() == null ? null : comment.getPost().getId())
                .targetType(targetType == null ? null : targetType.name())
                .targetId(targetId.isBlank() ? null : targetId)
                .creationDate(comment.getCreationDate())
                .build();
    }
}
