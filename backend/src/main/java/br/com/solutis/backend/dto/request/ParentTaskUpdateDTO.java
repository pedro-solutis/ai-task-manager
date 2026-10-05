package br.com.solutis.backend.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ParentTaskUpdateDTO(
    @NotNull (message = "Parent task id is required")
    UUID parentTaskId
) {

}
