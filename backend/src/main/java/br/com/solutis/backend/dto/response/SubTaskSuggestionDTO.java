package br.com.solutis.backend.dto.response;

import java.time.LocalDateTime;

import br.com.solutis.backend.domain.enums.TaskPriority;

public record SubTaskSuggestionDTO(
        String title,
        String description,
        TaskPriority priority,
        LocalDateTime dueDate
) {
}
