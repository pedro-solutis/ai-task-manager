package br.com.solutis.backend.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
            .maxMessages(10)
            .build();
    }

    @Bean
    @Primary
    @ConditionalOnProperty (name = "app.ai.provider", havingValue = "gemini", matchIfMissing = true)
    public ChatModel geminiChatModel(@Qualifier("googleGenAiChatModel") ChatModel gemini){
        return gemini;
    }

    @Bean
    @Primary
        @ConditionalOnProperty (name = "app.ai.provider", havingValue = "ollama", matchIfMissing = true)
    public ChatModel ollamaChatModel(@Qualifier("ollamaChatModel") ChatModel ollama){
        return ollama;
    }

    @Bean  
    public ChatClient.Builder chatClient(ChatModel chatModel){
        return ChatClient.builder(chatModel);
    }
}
