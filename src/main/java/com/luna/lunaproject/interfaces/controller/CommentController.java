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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{type}/{targetId}")
    public ResponseEntity<CommentResponseDTO> create(
            @PathVariable CommentTargetType type,
            @PathVariable String targetId,
            @Valid @RequestBody CommentRequestDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(type, targetId, body));
    }

    @GetMapping("/{type}/{targetId}")
    public ResponseEntity<List<CommentResponseDTO>> listByTarget(
            @PathVariable CommentTargetType type,
            @PathVariable String targetId) {
        return ResponseEntity.ok(commentService.listByTarget(type, targetId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable UUID id,
                                                     @Valid @RequestBody CommentRequestDTO body) {
        return ResponseEntity.ok(commentService.update(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.delete(id));
    }
}
