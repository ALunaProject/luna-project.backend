package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Comment;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CommentsReqDTO {
    @Size(max = 500)
    private String content;
    private LocalDate creationDate;

    public CommentsReqDTO() {
    }

    public CommentsReqDTO(String content, LocalDate creationDate) {
        this.content = content;
        this.creationDate = LocalDate.now();
    }

    public CommentsReqDTO(Comment comment) {
        this.content = comment.getContent();
        this.creationDate = LocalDate.now();
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
}
