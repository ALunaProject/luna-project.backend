package com.luna.lunaproject.services;

import com.luna.lunaproject.dto.PostRequestDTO;
import com.luna.lunaproject.dto.PostResponseDTO;
import com.luna.lunaproject.entity.Post;
import com.luna.lunaproject.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    exibir post - get
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> responseDTOs = posts.stream().map(PostResponseDTO::new).collect(toList());
        return responseDTOs;
    }

//    exibir post por id - get
    public List<PostResponseDTO> getPostById(int id) {
        Optional<Post> post = postRepository.findById(id);
        List<PostResponseDTO> responseDTO = post.stream().map(PostResponseDTO::new).toList();
        return responseDTO;
    }

//    criar post - post
    public ResponseEntity<?> savePost(PostRequestDTO postRequestDTO) {
        Post post = new Post(postRequestDTO);
        postRepository.save(post);
        return ResponseEntity.ok("Post saved successfully");
    }

//    editar post - put
    public ResponseEntity<?> editPost(int id, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(id).get();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        postRepository.save(post);
        return ResponseEntity.ok("Post edited successfully");
    }

//    apagar post - delete
    public ResponseEntity<?> deletePost(int id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.ok("Post deleted successfully");
        }else  {
            return ResponseEntity.ok("Post not found");
        }
    }
}
