package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.comments.CommentRequestDTO;
import com.luna.lunaproject.application.dto.comments.CommentResponseDTO;
import com.luna.lunaproject.application.dto.comments.CommentTargetResolver;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentTargetResolver targetResolver;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDTO create(CommentTargetType type, String rawTargetId, CommentRequestDTO dto) {
        String targetId = targetResolver.resolve(type, rawTargetId);

        Comment comment = Comment.builder()
                .content(dto.getContent().trim())
                .author(getAuthenticatedUser())
                .targetType(type)
                .targetId(targetId)
                .build();

        return toResponseDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> listByTarget(CommentTargetType type, String rawTargetId) {
        String targetId = targetResolver.resolve(type, rawTargetId);
        return commentRepository
                .findByTargetTypeAndTargetIdOrderByCreationDateDesc(type, targetId)
                .stream().map(this::toResponseDto).toList();
    }

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

    // editar: só o autor ou admin
    private void validateCanEdit(Comment comment) {
        User auth = getAuthenticatedUser();
        boolean isAuthor = comment.getAuthor().getId().equals(auth.getId());
        boolean isAdmin = auth.getRole() == UserRole.ADMIN;
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenOperationException("Você não tem permissão para alterar este comentário");
        }
    }

    // apagar: autor, admin, ou dono do perfil (moderar o próprio mural)
    private void validateCanDelete(Comment comment) {
        User auth = getAuthenticatedUser();
        boolean isAuthor = comment.getAuthor().getId().equals(auth.getId());
        boolean isAdmin = auth.getRole() == UserRole.ADMIN;
        boolean isProfileOwner = comment.getTargetType() == CommentTargetType.USER_PROFILE
                && comment.getTargetId().equals(auth.getId().toString());
        if (!isAuthor && !isAdmin && !isProfileOwner) {
            throw new ForbiddenOperationException("Você não tem permissão para apagar este comentário");
        }
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private CommentResponseDTO toResponseDto(Comment c) {
        return CommentResponseDTO.builder()
                .id(c.getId())
                .content(c.getContent())
                .userId(c.getAuthor().getId())
                .username(c.getAuthor().getUsername())
                .profilePicUrl(c.getAuthor().getProfilePicUrl())
                .targetType(c.getTargetType().name())
                .targetId(c.getTargetId())
                .creationDate(c.getCreationDate())
                .build();
    }
}
