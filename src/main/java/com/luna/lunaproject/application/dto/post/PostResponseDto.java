package com.luna.lunaproject.application.dto.post;

import com.luna.lunaproject.domain.entity.Post;
import com.luna.lunaproject.domain.model.Tag;
import com.luna.lunaproject.domain.model.TagCatalog;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostResponseDto {

    private UUID id;
    private String content;
    private LocalDate creation_date;
    private UUID user_id;
    private List<Tag> tags;

    // construtor de 4 args mantido pra não quebrar quem já o usa
    public PostResponseDto(UUID id, String content, LocalDate creation_date, UUID user_id) {
        this.id = id;
        this.content = content;
        this.creation_date = creation_date;
        this.user_id = user_id;
        this.tags = List.of();
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.creation_date = post.getCreateion_date();
        this.user_id = post.getUser().getId();
        this.tags = TagCatalog.resolve(post.getTagIds());
    }
}
