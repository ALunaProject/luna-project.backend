package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.Comments;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

public class CommentsResDTO {
    private String content;
    private LocalDate creationDate;


    public CommentsResDTO(CommentsReqDTO req) {
        this.content = req.getContent();
        this.creationDate = req.getCreationDate();
    }

    public CommentsResDTO(Comments comments) {
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
