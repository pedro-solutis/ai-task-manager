package br.com.solutis.backend.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskDecomposeRequestDTO(
    @NotBlank (message = "Title is required")
    String title,
    @NotBlank (message = "Description is required")
    String description,
    @Future (message = "Due date must be in the future")
    @NotNull (message = "Due date is required")
    LocalDateTime dueDate
) {

}
