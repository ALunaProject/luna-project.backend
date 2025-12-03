package com.luna.lunaproject.entity;

import com.luna.lunaproject.dto.PostRequestDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "user_id")
    private Integer userId;

    public Post() {
    }

    public Post(PostRequestDTO postRequestDTO) {
        this.title = postRequestDTO.getTitle();
        this.content = postRequestDTO.getContent();
    }


    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
