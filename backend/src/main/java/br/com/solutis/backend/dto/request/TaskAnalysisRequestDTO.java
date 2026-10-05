package br.com.solutis.backend.dto.request;

import br.com.solutis.backend.validation.ValidEnum;
import br.com.solutis.backend.domain.enums.TaskPriority;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskAnalysisRequestDTO(
    @NotBlank (message = "Title is required")
    String title,
    @NotBlank (message = "Description is required")
    String description,
    @ValidEnum(
        enumClass = TaskPriority.class,
        message = "Invalid priority. Accepted values are: LOW, MEDIUM, HIGH")
    String priority,
    @Future (message = "Due date must be in the future")
    @NotNull (message = "Due date is required")
    LocalDateTime dueDate
) {

}
