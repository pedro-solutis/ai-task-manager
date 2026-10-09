package br.com.solutis.backend.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatClientConfig {

    @Value ("${app.ai.context.guardrails}")
    private Resource commonGuardrails;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
            .maxMessages(10)
            .build();
    }

    @Bean(name = "geminiClient")
    public ChatClient geminiChatClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel gemini){
        return ChatClient.builder(gemini).defaultSystem(commonGuardrails).build();
    }

    @Bean(name = "ollamaClient")
    public ChatClient ollamaChatClientBuilder(@Qualifier("ollamaChatModel") ChatModel ollama){
        return ChatClient.builder(ollama).defaultSystem(commonGuardrails).build();
    }
}
