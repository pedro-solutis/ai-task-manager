package br.com.solutis.backend.adapter.ai;

import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Primary
@RequiredArgsConstructor
public class GeminiAdapter implements AiModelAdapter {

    private final GoogleGenAiChatModel chatModel;

    @Override
    public String generateText(String prompt) {
        return chatModel.call(prompt);
    }
}
