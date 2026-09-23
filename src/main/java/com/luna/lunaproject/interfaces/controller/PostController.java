package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.comments.CommentResponseDTO;
import com.luna.lunaproject.application.dto.post.PostRequestDto;
import com.luna.lunaproject.application.dto.post.PostResponseDto;
import com.luna.lunaproject.application.services.CommentService;
import com.luna.lunaproject.application.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostRequestDto postRequestDto) {
        PostResponseDto response = postService.createPost(postRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPostById(@PathVariable UUID id) {
        PostResponseDto response = postService.getPostById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllPosts() {
        List<PostResponseDto> response = postService.getAllPosts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(id));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Object> getCommentsByPost(@PathVariable UUID postId) {
        List<CommentResponseDTO> response = commentService.getCommentsByPost(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
