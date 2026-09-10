package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.post.PostRequestDto;
import com.luna.lunaproject.application.dto.post.PostResponseDto;
import com.luna.lunaproject.application.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody PostRequestDto postRequestDto) {
        PostResponseDto response = postService.createPost(postRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        PostResponseDto response = postService.getPostById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(id));
    }
}
