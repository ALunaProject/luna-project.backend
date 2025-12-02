package com.luna.lunaproject.dto;

public class PostResponseDTO {

    private String tittle;
    private String content;

    public PostResponseDTO(PostRequestDTO postRequestDTO) {
        this.tittle = postRequestDTO.getTitle();
        this.content = postRequestDTO.getContent();
    }

    public String getTittle() {
        return tittle;
    }

    public String getContent() {
        return content;
    }
}
