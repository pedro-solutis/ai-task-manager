package br.com.solutis.backend.dto.response;

import br.com.solutis.backend.domain.enums.TaskComplexity;
import br.com.solutis.backend.domain.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO representing task analysis response")
public record TaskAnalysisResponseDTO(
        @Schema(description = "Priority of the task", example = "MEDIUM")
        @NotNull(message = "Priority is required")
        TaskPriority priority,
        @Schema(description = "Complexity of the task", example = "MODERATE")
        @NotNull(message = "Complexity is required")
        TaskComplexity complexity,
        @Schema(description = "Estimated hours for the task", example = "5")
        @NotNull(message = "Estimated hours is required")
        @Min(value = 1, message = "Estimated hours must be greater than 0")
        Integer estimatedHours,
        @Schema(description = "Reason for the analysis", example = "Task requires refactoring")
        @NotBlank(message = "Analysis reason is required")
        String analysisReason
) {
}
