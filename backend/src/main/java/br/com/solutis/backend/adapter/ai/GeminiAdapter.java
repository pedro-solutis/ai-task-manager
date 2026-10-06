package br.com.solutis.backend.adapter.ai;

import br.com.solutis.backend.tools.TaskTools;
import br.com.solutis.backend.dto.response.*;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Primary
@RequiredArgsConstructor
public class GeminiAdapter implements AiModelAdapter {

    private final ChatClient chatClient;
    private final TaskTools taskTools;
    private final ChatMemory chatMemory;

    @Override
    public String generateText(String prompt) {
        return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
    }

    @Override
    public ChatResponseDTO chat(String chatId, String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .tools(taskTools)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(a -> a.param("chat_memory_conversation_id", chatId))
                .call()
                .entity(ChatResponseDTO.class);
    }
}
