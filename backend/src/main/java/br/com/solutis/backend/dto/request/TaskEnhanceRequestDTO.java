package br.com.solutis.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TaskEnhanceRequestDTO(
    @NotBlank (message = "Title is required")
    String title,
    @NotBlank (message = "Description is required")
    String description
) {

}
