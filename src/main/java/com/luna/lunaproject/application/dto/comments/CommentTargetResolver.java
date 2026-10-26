package com.luna.lunaproject.application.dto.comments;

import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.application.services.BusinessRuleException;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import com.luna.lunaproject.domain.repository.PostRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentTargetResolver {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public String resolve(CommentTargetType type, String rawId) {
        String id = rawId == null ? "" : rawId.trim();
        if (id.isEmpty() || id.length() > 100) {
            throw new BusinessRuleException("Id do alvo inválido");
        }

        return switch (type) {
            case USER_PROFILE -> {
                UUID uuid = parseUuid(id);
                if (!userRepository.existsById(uuid)) {
                    throw new ResourceNotFoundException("User not found with id: " + uuid);
                }
                yield uuid.toString();
            }
            case POST -> {
                UUID uuid = parseUuid(id);
                if (!postRepository.existsById(uuid)) {
                    throw new ResourceNotFoundException("Post not found with id: " + uuid);
                }
                yield uuid.toString();
            }
            case GAME -> {
                try {
                    int appid = Integer.parseInt(id);
                    if (appid <= 0) throw new NumberFormatException();
                    yield String.valueOf(appid);
                } catch (NumberFormatException e) {
                    throw new BusinessRuleException("appid inválido");
                }
            }
            case NEWS -> id;
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
