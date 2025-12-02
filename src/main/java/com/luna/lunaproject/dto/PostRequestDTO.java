package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Post;
import jakarta.validation.constraints.*;

public class PostRequestDTO {

    @NotBlank
    @Size(min = 1, max = 25)
    private String title;
    @Size(max = 500)
    private String content;

    public PostRequestDTO() {
    }
    public PostRequestDTO(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
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

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
