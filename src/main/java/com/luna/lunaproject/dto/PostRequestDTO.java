package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Post;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PostRequestDTO {

    @NotBlank
    @Size(min = 1, max = 25)
    private String title;
    @Size(max = 500)
    private String content;
    private LocalDate creationDate;

    public PostRequestDTO() {
    }

    public PostRequestDTO(String title, String content, LocalDate creationDate) {
        this.title = title;
        this.content = content;
        this.creationDate = LocalDate.now();
    }

    public PostRequestDTO(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.creationDate = LocalDate.now();
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

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
