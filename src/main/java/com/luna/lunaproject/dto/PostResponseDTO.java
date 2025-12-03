package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Post;

import java.time.LocalDate;

public class PostResponseDTO {

    private String title;
    private String content;
    private LocalDate creationDate;


    public PostResponseDTO(Post postRequestDTO) {
        this.title = postRequestDTO.getTitle();
        this.content = postRequestDTO.getContent();
        this.creationDate = postRequestDTO.getCreationDate();
    }

    public String getTittle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
