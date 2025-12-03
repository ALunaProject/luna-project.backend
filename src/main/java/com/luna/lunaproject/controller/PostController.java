package com.luna.lunaproject.controller;

import com.luna.lunaproject.dto.PostRequestDTO;
import com.luna.lunaproject.dto.PostResponseDTO;
import com.luna.lunaproject.entity.Post;
import com.luna.lunaproject.repository.PostRepository;
import com.luna.lunaproject.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/exibir")
    public List<PostResponseDTO>  getPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/exibir/{id}")
    public List<PostResponseDTO> getPosts(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping("/postar")
    public ResponseEntity<?> criarPost(@RequestBody PostRequestDTO postRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(postRequestDTO));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarPost(@RequestBody PostRequestDTO postRequestDTO, @PathVariable int id) {
        return ResponseEntity.ok(postService.editPost(id, postRequestDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }
}
