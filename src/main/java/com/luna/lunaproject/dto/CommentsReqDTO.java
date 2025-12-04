package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class CommentsReqDTO {
    @NotBlank
    @Size(max = 255)
    private String content;
    private LocalDate creationDate;


    private CommentsReqDTO() {}

    public CommentsReqDTO(String content) {
        this.content = content;
        this.creationDate = LocalDate.now();
    }

    public CommentsReqDTO(Comment comment) {
        this.content = comment.getContent();
        this.creationDate = comment.getCreationDate();
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

    @Override
    public String toString() {
        return "CommentsReqDTO{" +
                "content='" + content + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
