package com.luna.lunaproject.entity;

import com.luna.lunaproject.dto.CommentsReqDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "post")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer commentId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "user_id")
    private Integer userId;

    public Comment() {
    }

    public Comment(String title, String content, LocalDate creationDate) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
    }

    public Comment(CommentRequestDTO postRequestDTO) {
        this.title = postRequestDTO.getTitle();
        this.content = postRequestDTO.getContent();
        this.creationDate = LocalDate.now();
    }


    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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
