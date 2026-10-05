package br.com.solutis.backend.dto.response;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    TaskPriority priority,
    LocalDateTime dueDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID parentTaskId
) {}
