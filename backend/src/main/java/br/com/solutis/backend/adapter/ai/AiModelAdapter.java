package br.com.solutis.backend.adapter.ai;

import br.com.solutis.backend.dto.response.*;

public interface AiModelAdapter {
    String generateText(String prompt);
    ChatResponseDTO chat(String chatId, String prompt);
}
