package br.com.solutis.backend.dto.request;

import br.com.solutis.backend.validation.ValidEnum;
import br.com.solutis.backend.domain.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object for Task Analysis Request")
public record TaskAnalysisRequestDTO(
    @Schema(description = "The title of the task", example = "Implement login")
    @NotBlank (message = "Title is required")
    String title,
    
    @Schema(description = "The description of the task", example = "Implement JWT based authentication")
    @NotBlank (message = "Description is required")
    String description,
    
    @Schema(description = "The priority of the task", example = "HIGH")
    @ValidEnum(
        enumClass = TaskPriority.class,
        message = "Invalid priority. Accepted values are: LOW, MEDIUM, HIGH")
    String priority,
    
    @Schema(description = "The due date of the task", example = "2024-12-31T23:59:59")
    @Future (message = "Due date must be in the future")
    @NotNull (message = "Due date is required")
    LocalDateTime dueDate
) {

}
