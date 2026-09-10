package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.post.PostRequestDto;
import com.luna.lunaproject.application.dto.post.PostResponseDto;
import com.luna.lunaproject.domain.entity.Post;
import com.luna.lunaproject.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setCreateion_date(LocalDate.now());
        Post savedPost = postRepository.save(post);

        return new PostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreateion_date()

        );
    }

    public PostResponseDto getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + postId));

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreateion_date()
        );
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
        return "User not found with id: " + postId;
        }
    }


}
