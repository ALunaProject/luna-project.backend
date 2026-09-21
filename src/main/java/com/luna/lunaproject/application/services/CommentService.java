package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.comments.CommentRequestDTO;
import com.luna.lunaproject.application.dto.comments.CommentResponseDTO;
import com.luna.lunaproject.domain.entity.Comment;
import com.luna.lunaproject.domain.entity.Post;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.UserRole;
import com.luna.lunaproject.domain.repository.CommentRepository;
import com.luna.lunaproject.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        User user = getAuthenticatedUser();

        Post post = postRepository.findById(commentRequestDTO.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + commentRequestDTO.getPostId()));

        Comment comment = Comment.builder()
                .content(commentRequestDTO.getContent())
                .user(user)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return toResponseDto(savedComment);
    }

    public CommentResponseDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        return toResponseDto(comment);
    }

    public List<CommentResponseDTO> getCommentsByPost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        List<Comment> comments = commentRepository.findByPostIdOrderByCreationDateAsc(postId);
        return comments.stream().map(this::toResponseDto).toList();
    }

    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        validateOwnership(comment);

        comment.setContent(commentRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return toResponseDto(updatedComment);
    }

    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment == null) {
            return "Comment not found with id: " + commentId;
        }

        validateOwnership(comment);
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }

    private void validateOwnership(Comment comment) {
        User authenticatedUser = getAuthenticatedUser();

        boolean isOwner = comment.getUser().getId().equals(authenticatedUser.getId());
        boolean isAdmin = authenticatedUser.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenOperationException("Você não tem permissão para alterar este comentário");
        }
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private CommentResponseDTO toResponseDto(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .postId(comment.getPost().getId())
                .creationDate(comment.getCreationDate())
                .build();
    }
}
