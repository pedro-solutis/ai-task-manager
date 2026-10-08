package br.com.solutis.backend.dto.response;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO representing task decompose response")
public record TaskDecomposeResponseDTO(
        @Schema(description = "List of decomposed sub-tasks")
        @NotNull(message = "Subtasks list cannot be null")
        @NotEmpty(message = "Subtasks list cannot be empty")
        List<@Valid SubTaskDTO> subTasks
) {
}
