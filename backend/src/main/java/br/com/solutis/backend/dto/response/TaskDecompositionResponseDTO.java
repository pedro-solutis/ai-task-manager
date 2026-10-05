package br.com.solutis.backend.dto.response;

import java.util.List;

public record TaskDecompositionResponseDTO(
        List<SubTaskSuggestionDTO> subTasks
) {
}
