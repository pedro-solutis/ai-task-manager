package br.com.solutis.backend.adapter.ai;

import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class GeminiAdapter implements AiModelAdapter {

    private GoogleGenAiChatModel chatModel;

    public GeminiAdapter(){
        this.chatModel = GoogleGenAiChatModel.builder()
        .build();
    }

    @Override
    public String generateText(String prompt) {
        return chatModel.call(prompt);
    }
}
