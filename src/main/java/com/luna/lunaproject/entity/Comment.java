package com.luna.lunaproject.entity;

import com.luna.lunaproject.dto.CommentsReqDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "comment_id")
    private int commentId;
    @Column(name = "content", nullable = false, length = 255)
    private String content;
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "post_id", nullable = false)
    private int postId;

    public Comment() {
    }

    //    alterar depois para requestDTO quando criar od DTO dessa entidade
    public Comment(CommentsReqDTO req) {
        this.content = req.getContent();
        this.creationDate = req.getCreationDate();

    }

    public Comment(int commentId, String content, int userId, int postId) {
        this.commentId = commentId;
        this.content = content;
        this.creationDate = LocalDate.now();
        this.userId = userId;
        this.postId = postId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
