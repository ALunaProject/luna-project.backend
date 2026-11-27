package com.luna.lunaproject.application.dto.post;

import com.luna.lunaproject.domain.model.TagCatalog;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostRequestDto {

    private String content;

    // Opcional. null = "não mexer nas tags" (o PUT de edição hoje só manda o content e não pode apagá-las)
    @Size(max = TagCatalog.MAX_TAGS_PER_POST, message = "Um post pode ter no máximo " + TagCatalog.MAX_TAGS_PER_POST + " tags")
    private Set<Integer> tagIds;
}
