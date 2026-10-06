package br.com.solutis.backend.dto.response;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TaskDecomposeResponseDTO(
        @NotNull(message = "Subtasks list cannot be null")
        @NotEmpty(message = "Subtasks list cannot be empty")
        @Valid
        List<SubTaskDTO> subTasks
) {
}
