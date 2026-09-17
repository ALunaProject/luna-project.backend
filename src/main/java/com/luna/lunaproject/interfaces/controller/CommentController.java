package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.comments.CommentRequestDTO;
import com.luna.lunaproject.application.dto.comments.CommentResponseDTO;
import com.luna.lunaproject.application.services.CommentService;
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

    @PostMapping
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO response = commentService.createComment(commentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCommentById(@PathVariable Long id) {
        CommentResponseDTO response = commentService.getCommentById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Object> getCommentsByPost(@PathVariable UUID postId) {
        List<CommentResponseDTO> response = commentService.getCommentsByPost(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateComment(@PathVariable Long id, @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO response = commentService.updateComment(id, commentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(id));
    }
}
