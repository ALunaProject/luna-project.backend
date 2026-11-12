package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.post.PostRequestDto;
import com.luna.lunaproject.application.dto.post.PostResponseDto;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.Post;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.model.TagCatalog;
import com.luna.lunaproject.domain.repository.PostRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto, UUID user_id) {
        Post post = new Post();
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        post.setContent(postRequestDto.getContent());
        post.setCreateion_date(LocalDate.now());
        post.setCreatedAt(Instant.now());
        post.setUser(user);
        post.setTagIds(TagCatalog.requireValid(postRequestDto.getTagIds()));
        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    public PostResponseDto getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + postId));

        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getAllPosts() {
        List<Post> post = postRepository.findAll();
        return post.stream().map(PostResponseDto::new).toList();
    }

    public String deletePost(UUID postId) {
        if  (postRepository.findById(postId).isPresent()) {
            postRepository.deleteById(postId);
            return "Post deleted successfully";
        }else {
        return "Post not found with id: " + postId;
        }
    }

    public String updatePost(UUID postId, PostRequestDto postRequestDto) {
        if  (postRepository.findById(postId).isPresent()) {
            Post post = postRepository.findById(postId).get();
            post.setContent(postRequestDto.getContent());
            postRepository.save(post);
            return "Post updated successfully";
        }else  {
        return "Post not found with id: " + postId;
        }
    }
}
