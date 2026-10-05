package br.com.solutis.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskAiRequestDTO(
    @NotBlank (message = "Title is required")
    String title,
    @NotBlank (message = "Description is required")
    String description
) {

}
