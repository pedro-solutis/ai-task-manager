package br.com.solutis.backend.dto.response;

import java.time.LocalDateTime;

import br.com.solutis.backend.domain.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO representing a sub-task")
public record SubTaskDTO(
        @Schema(description = "Title of the sub-task", example = "Fix bug")
        @NotBlank(message = "Title is required")
        String title,
        @Schema(description = "Description of the sub-task", example = "Fix the login bug")
        @NotBlank(message = "Description is required")
        String description,
        @Schema(description = "Priority of the sub-task", example = "HIGH")
        @NotNull(message = "Priority is required")
        TaskPriority priority,
        @Schema(description = "Due date of the sub-task", example = "2024-12-31T23:59:59")
        @NotNull(message = "Due date is required")
        @Future(message = "Due date must be in the future")
        LocalDateTime dueDate
) {
}
