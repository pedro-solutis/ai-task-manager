package br.com.solutis.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data Transfer Object for Task Enhancement Request")
public record TaskEnhanceRequestDTO(
    @Schema(description = "The title of the task", example = "Fix bug")
    @NotBlank (message = "Title is required")
    String title,
    
    @Schema(description = "The description of the task", example = "Bug is happening when clicking button")
    @NotBlank (message = "Description is required")
    String description
) {

}
