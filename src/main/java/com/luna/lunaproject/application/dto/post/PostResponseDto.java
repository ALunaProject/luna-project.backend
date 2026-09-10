package com.luna.lunaproject.application.dto.post;

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
}
