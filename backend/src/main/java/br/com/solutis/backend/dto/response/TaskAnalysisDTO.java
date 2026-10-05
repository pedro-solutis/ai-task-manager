package br.com.solutis.backend.dto.response;

import br.com.solutis.backend.domain.enums.TaskComplexity;
import br.com.solutis.backend.domain.enums.TaskPriority;

public record TaskAnalysisDTO(
        TaskPriority priority,
        TaskComplexity complexity,
        Integer estimatedHours,
        String analysisReason
) {
}
