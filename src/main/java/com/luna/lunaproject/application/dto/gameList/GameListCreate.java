package com.luna.lunaproject.application.dto.gameList;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameListCreate {
    @NotNull(message = "Can't be null")
    @Size(min = 2, max = 36)
    private String name;
}