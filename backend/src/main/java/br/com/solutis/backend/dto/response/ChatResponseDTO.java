package br.com.solutis.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a chat response")
public record ChatResponseDTO(
    @Schema(description = "ID of the chat", example = "chat-123")
    String chatId,
    @Schema(description = "Response text of the chat", example = "Hello world!")
    String chatResponse
) {

}
