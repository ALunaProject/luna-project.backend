package com.luna.lunaproject.application.dto.reports;

import com.luna.lunaproject.application.exceptions.BusinessRuleException;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.enums.ReportTargetType;
import com.luna.lunaproject.domain.repository.CommentRepository;
import com.luna.lunaproject.domain.repository.PostRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReportTargetResolver {

    public record ResolvedTarget(String targetId, UUID ownerId) {}

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public ResolvedTarget resolve(ReportTargetType type, String rawId) {
        String id = rawId == null ? "" : rawId.trim();
        if (id.isEmpty() || id.length() > 100) {
            throw new BusinessRuleException("Id do alvo inválido");
        }
        UUID uuid = parseUuid(id);

        return switch (type) {
            case USER_PROFILE -> {
                if (!userRepository.existsById(uuid)) {
                    throw new ResourceNotFoundException("User not found with id: " + uuid);
                }
                yield new ResolvedTarget(uuid.toString(), uuid);
            }
            case POST -> {
                var post = postRepository.findById(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + uuid));
                yield new ResolvedTarget(uuid.toString(), post.getUser().getId());
            }
            case COMMENT -> {
                var comment = commentRepository.findById(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + uuid));
                yield new ResolvedTarget(uuid.toString(), comment.getAuthor().getId());
            }
        };
    }

    private UUID parseUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Id inválido: " + id);
        }
    }
}