package com.luna.lunaproject.application.dto.reports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 255, message = "Description needs to be at least 255 characters")
    private String description;
}