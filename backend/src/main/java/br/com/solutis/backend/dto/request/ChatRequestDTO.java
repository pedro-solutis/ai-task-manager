package br.com.solutis.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data Transfer Object for Chat Requests")
public record ChatRequestDTO(
    @Schema(description = "The chat ID", example = "12345")
    @Size(min = 5, message = "Chat Id must have a minimum of 5 characters") 
    String chatId,
    
    @Schema(description = "The prompt to send", example = "Hello, world!")
    @NotBlank (message = "Prompt is required")
    String prompt
) {

}
