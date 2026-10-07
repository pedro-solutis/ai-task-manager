package br.com.solutis.backend.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.validation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for Task Creation")
public record TaskCreationDTO(
    @Schema(description = "The title of the task", example = "Create documentation")
    @NotBlank(message = "Title is required")
    String title,
    
    @Schema(description = "The description of the task", example = "Write API documentation for the project")
    @NotBlank(message = "Description is required")
    String description,
    
    @Schema(description = "The priority of the task", example = "MEDIUM")
    @NotNull(message = "Priority is required")
    @ValidEnum (enumClass = TaskPriority.class,
        message = "Invalid priority. Accepted values are: LOW, MEDIUM, HIGH"
    )
    String priority,
    
    @Schema(description = "The due date of the task", example = "2024-12-31T23:59:59")
    @Future (message = "Due date must be in the future")
    @NotNull(message = "Due date is required")
    LocalDateTime dueDate,
    
    @Schema(description = "The parent task ID if applicable", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID parentTaskId
) {}
