package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Comment;

import java.time.LocalDate;

public class CommentsResDTO {
    private String content;
    private LocalDate creationDate;


    public CommentsResDTO(CommentsReqDTO req) {
        this.content = req.getContent();
        this.creationDate = req.getCreationDate();
    }

    public CommentsResDTO(Comment comment) {
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
