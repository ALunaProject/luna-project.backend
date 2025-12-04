package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Comments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public CommentsReqDTO(Comments comments) {
        this.content = comments.getContent();
        this.creationDate = comments.getCreationDate();
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
