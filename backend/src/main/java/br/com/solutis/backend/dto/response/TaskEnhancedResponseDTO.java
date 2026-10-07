package br.com.solutis.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO representing enhanced task response")
public record TaskEnhancedResponseDTO(
        @Schema(description = "Enhanced title of the task", example = "Improved login flow")
        @NotBlank(message = "Title is required")
        String title, 
        @Schema(description = "Enhanced description of the task", example = "Detailed steps to improve login")
        @NotBlank(message = "Description is required")
        String description
) {
}
