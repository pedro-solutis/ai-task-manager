package br.com.solutis.backend.adapter.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Primary
@RequiredArgsConstructor
public class GeminiAdapter implements AiModelAdapter {

    private final ChatClient chatClient;

    @Override
    public String generateText(String prompt) {
        return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
    }

    @Override
    public String chat(String chatId, String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param("chat_memory_conversation_id", chatId))
                .call()
                .content();
    }
}
