package br.com.solutis.backend.dto.response;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "DTO representing a task response")
public record TaskResponseDTO(
    @Schema(description = "ID of the task", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    @Schema(description = "Title of the task", example = "Complete assignment")
    String title,
    @Schema(description = "Description of the task", example = "Complete the Java programming assignment")
    String description,
    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    TaskStatus status,
    @Schema(description = "Priority of the task", example = "HIGH")
    TaskPriority priority,
    @Schema(description = "Due date of the task", example = "2024-12-31T23:59:59")
    LocalDateTime dueDate,
    @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
    LocalDateTime createdAt,
    @Schema(description = "Last update timestamp", example = "2024-01-02T12:00:00")
    LocalDateTime updatedAt,
    @Schema(description = "ID of the parent task, if any", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID parentTaskId,
    @Schema(description = "List of sub-tasks")
    List<TaskResponseDTO> subTasks
) {}
