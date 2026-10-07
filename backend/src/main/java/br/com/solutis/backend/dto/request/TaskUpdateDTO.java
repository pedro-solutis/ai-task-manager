package br.com.solutis.backend.dto.request;

import java.time.LocalDateTime;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.validation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

@Schema(description = "Data Transfer Object for updating a task")
public record TaskUpdateDTO(
    @Schema(description = "The updated title of the task", example = "Update documentation")
    @Size (min = 3, message = "Title should have a minimum of 3 characters.")
    String title,
    
    @Schema(description = "The updated description of the task", example = "Update the README file with new instructions")
    @Size (min = 5, message = "Description should have a minimum of 5 characters.")
    String description,
    
    @Schema(description = "The updated priority of the task", example = "HIGH")
    @ValidEnum (enumClass = TaskPriority.class, message = "Invalid Priority. Accepted values: LOW, MEDIUM, HIGH")
    String priority,
    
    @Schema(description = "The updated due date of the task", example = "2024-12-31T23:59:59")
    @Future (message = "Due date must be in the future")
    LocalDateTime dueDate
) {
    
}
