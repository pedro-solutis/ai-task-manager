package br.com.solutis.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.validation.ValidEnum;

public record TaskCreationDTO(
    @NotBlank(message = "Title is required")
    String title,
    @NotBlank(message = "Description is required")
    String description,
    @NotNull(message = "Priority is required")
    @ValidEnum (enumClass = TaskPriority.class,
        message = "Invalid priority. Accepted values are: LOW, MEDIUM, HIGH"
    )
    String priority,
    @Future (message = "Due date must be in the future")
    @NotNull(message = "Due date is required")
    LocalDateTime dueDate,
    UUID parentTaskId
) {}
