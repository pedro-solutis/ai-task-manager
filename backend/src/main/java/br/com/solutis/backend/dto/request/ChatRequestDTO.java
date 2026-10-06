package br.com.solutis.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequestDTO(
    @Size(min = 5, message = "Chat Id must have a minimum of 5 characters") 
    String chatId,
    @NotBlank (message = "Prompt is required")
    String prompt
) {

}
