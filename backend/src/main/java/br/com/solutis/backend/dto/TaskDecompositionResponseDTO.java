package br.com.solutis.backend.dto;

import java.util.List;

public record TaskDecompositionResponseDTO(
        List<SubTaskSuggestionDTO> subTasks
) {
}
