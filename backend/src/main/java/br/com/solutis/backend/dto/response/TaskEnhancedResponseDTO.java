package br.com.solutis.backend.dto.response;

import jakarta.validation.constraints.NotBlank;

public record TaskEnhancedResponseDTO(
        @NotBlank(message = "Title is required")
        String title, 
        @NotBlank(message = "Description is required")
        String description
) {
}
