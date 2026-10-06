package br.com.solutis.backend.dto.response;

import br.com.solutis.backend.domain.enums.TaskComplexity;
import br.com.solutis.backend.domain.enums.TaskPriority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskAnalysisResponseDTO(
        @NotNull(message = "Priority is required")
        TaskPriority priority,
        @NotNull(message = "Complexity is required")
        TaskComplexity complexity,
        @NotNull(message = "Estimated hours is required")
        @Min(value = 1, message = "Estimated hours must be greater than 0")
        Integer estimatedHours,
        @NotBlank(message = "Analysis reason is required")
        String analysisReason
) {
}
