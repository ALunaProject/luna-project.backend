package com.luna.lunaproject.controller;

import com.luna.lunaproject.dto.PostRequestDTO;
import com.luna.lunaproject.dto.PostResponseDTO;
import com.luna.lunaproject.entity.Post;
import com.luna.lunaproject.repository.PostRepository;
import com.luna.lunaproject.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/display")
    public List<PostResponseDTO>  getPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/display/{id}")
    public List<PostResponseDTO> getPosts(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping("/post")
    public ResponseEntity<?> criarPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(postRequestDTO));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editarPost(@Valid @RequestBody PostRequestDTO postRequestDTO, @PathVariable int id) {
        return ResponseEntity.ok(postService.editPost(id, postRequestDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }
}
