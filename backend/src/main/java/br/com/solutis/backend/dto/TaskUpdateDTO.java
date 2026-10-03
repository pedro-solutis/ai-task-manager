package br.com.solutis.backend.dto;

import java.time.LocalDateTime;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.validation.ValidEnum;
import jakarta.validation.constraints.Future;

public record TaskUpdateDTO(
    String description,
    @ValidEnum (enumClass = TaskPriority.class, message = "Priority inválida. Valores aceitos: LOW, MEDIUM, HIGH")
    String priority,
    @Future (message = "Due date must be in the future")
    LocalDateTime dueDate
) {
    
}
