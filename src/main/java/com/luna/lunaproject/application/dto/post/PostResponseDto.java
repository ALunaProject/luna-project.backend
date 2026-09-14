package com.luna.lunaproject.application.dto.post;

import com.luna.lunaproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostResponseDto {

    private UUID id;
    private String title;
    private String content;
    private LocalDate creation_date;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.creation_date = post.getCreateion_date();
    }
}
