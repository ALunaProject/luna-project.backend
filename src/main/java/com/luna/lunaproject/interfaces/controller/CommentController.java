package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.comments.CommentRequestDTO;
import com.luna.lunaproject.application.dto.comments.CommentResponseDTO;
import com.luna.lunaproject.application.services.CommentService;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Compatibilidade com o contrato antigo ({ "postId", "content" }) e suporte ao novo contrato genérico.
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentRequestDTO dto) {
        CommentResponseDTO response;
        if (dto.getTargetType() != null && dto.getTargetId() != null) {
            response = commentService.create(dto.getTargetType(), dto.getTargetId(), dto);
        } else if (dto.getPostId() != null) {
            response = commentService.create(CommentTargetType.POST, dto.getPostId().toString(), dto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe o postId ou o tipo e o id do destino do comentário");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/target/{type}/{targetId}")
    public ResponseEntity<CommentResponseDTO> createForTarget(
            @PathVariable CommentTargetType type,
            @PathVariable String targetId,
            @Valid @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(type, targetId, dto));
    }

    @GetMapping("/target/{type}/{targetId}")
    public ResponseEntity<List<CommentResponseDTO>> listByTarget(
            @PathVariable CommentTargetType type,
            @PathVariable String targetId) {
        return ResponseEntity.ok(commentService.listByTarget(type, targetId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable UUID id,
            @Valid @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.delete(id));
    }
}
