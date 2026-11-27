package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.post.PostRequestDto;
import com.luna.lunaproject.application.dto.post.PostResponseDto;
import com.luna.lunaproject.application.exceptions.ForbiddenOperationException;
import com.luna.lunaproject.application.exceptions.ResourceNotFoundException;
import com.luna.lunaproject.domain.entity.Post;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.CommentTargetType;
import com.luna.lunaproject.domain.enums.UserRole;
import com.luna.lunaproject.domain.model.TagCatalog;
import com.luna.lunaproject.domain.repository.CommentRepository;
import com.luna.lunaproject.domain.repository.PostRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

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
        return new PostResponseDto(findOrThrow(postId));
    }

    public List<PostResponseDto> getAllPosts() {
        List<Post> post = postRepository.findAll();
        return post.stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public String deletePost(UUID postId) {
        Post post = findOrThrow(postId);
        validateOwnershipOrAdmin(post);

        // apaga os comentários do post para não ficarem órfãos
        commentRepository.deleteByTargetTypeAndTargetId(CommentTargetType.POST, postId.toString());
        postRepository.delete(post);
        return "Post deleted successfully";
    }

    @Transactional
    public String updatePost(UUID postId, PostRequestDto postRequestDto) {
        Post post = findOrThrow(postId);
        validateOwnershipOrAdmin(post);

        post.setContent(postRequestDto.getContent());
        postRepository.save(post);
        return "Post updated successfully";
    }

    private Post findOrThrow(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }

    // editar/apagar: só o autor ou admin
    private void validateOwnershipOrAdmin(Post post) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAuthor = post.getUser().getId().equals(auth.getId());
        boolean isAdmin = auth.getRole() == UserRole.ADMIN;
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenOperationException("Você não tem permissão para alterar este post");
        }
    }
}