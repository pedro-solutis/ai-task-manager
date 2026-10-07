package br.com.solutis.backend.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object for Task Decomposition Request")
public record TaskDecomposeRequestDTO(
    @Schema(description = "The title of the task", example = "Large feature implementation")
    @NotBlank (message = "Title is required")
    String title,
    
    @Schema(description = "The description of the task", example = "Needs to be broken down into subtasks")
    @NotBlank (message = "Description is required")
    String description,
    
    @Schema(description = "The due date of the task", example = "2024-12-31T23:59:59")
    @Future (message = "Due date must be in the future")
    @NotNull (message = "Due date is required")
    LocalDateTime dueDate
) {

}
