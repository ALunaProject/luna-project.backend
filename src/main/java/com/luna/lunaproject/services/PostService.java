package com.luna.lunaproject.services;

import com.luna.lunaproject.dto.PostRequestDTO;
import com.luna.lunaproject.dto.PostResponseDTO;
import com.luna.lunaproject.entity.Post;
import com.luna.lunaproject.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    exibir post - get
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> responseDTOs = posts.stream().map(PostResponseDTO::new).toList();
        return responseDTOs;
    }

//    criar post - post
    public ResponseEntity<?> savePost(PostRequestDTO postRequestDTO) {
        Post post = new Post(postRequestDTO);
        postRepository.save(post);
        return ResponseEntity.ok("Post saved successfully");
    }

//    editar post - put
    public ResponseEntity<?> editarPost(int id, PostRequestDTO postRequestDTO) {

    }

//    apagar post - delete
    public ResponseEntity<?> deletePost(int id) {

    }
}
