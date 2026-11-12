package com.luna.lunaproject.application.dto.comments;

import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import com.luna.lunaproject.domain.repository.PostRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentTargetResolver {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public String resolve(CommentTargetType type, String rawTargetId) {
        if (type == null || rawTargetId == null || rawTargetId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O destino do comentário é obrigatório");
        }

        String targetId = rawTargetId.trim();
        try {
            return switch (type) {
                case POST -> {
                    UUID postId = UUID.fromString(targetId);
                    if (!postRepository.existsById(postId)) {
                        throw new ResourceNotFoundException("Post not found with id: " + postId);
                    }
                    yield postId.toString();
                }
                case USER_PROFILE -> {
                    UUID userId = UUID.fromString(targetId);
                    if (!userRepository.existsById(userId)) {
                        throw new ResourceNotFoundException("User not found with id: " + userId);
                    }
                    yield userId.toString();
                }
                case GAME -> {
                    int appId = Integer.parseInt(targetId);
                    if (appId <= 0) {
                        throw new NumberFormatException("appId must be positive");
                    }
                    yield Integer.toString(appId);
                }
            };
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador do destino inválido", ex);
        }
    }
}
