package br.com.solutis.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

public record TaskAiRequestDTO(
    @NotBlank (message = "Title is required")
    String title,
    @NotBlank (message = "Description is required")
    String description,
    @Future (message = "Due date must be in the future")
    LocalDateTime dueDate
) {

}
