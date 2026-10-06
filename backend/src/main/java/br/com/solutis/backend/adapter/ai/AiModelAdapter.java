package br.com.solutis.backend.adapter.ai;

public interface AiModelAdapter {
    String generateText(String prompt);
    String chat(String chatId, String prompt);
}
