package br.com.solutis.backend.dto.request;

import java.time.LocalDateTime;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.validation.ValidEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

public record TaskUpdateDTO(
    @Size (min = 3, message = "Title should have a minimum of 3 characters.")
    String title,
    @Size (min = 5, message = "Description should have a minimum of 5 characters.")
    String description,
    @ValidEnum (enumClass = TaskPriority.class, message = "Invalid Priority. Accepted values: LOW, MEDIUM, HIGH")
    String priority,
    @Future (message = "Due date must be in the future")
    LocalDateTime dueDate
) {
    
}
