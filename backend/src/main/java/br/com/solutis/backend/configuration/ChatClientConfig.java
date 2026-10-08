package br.com.solutis.backend.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
            .maxMessages(10)
            .build();
    }

    @Bean(name = "gemini")
    public ChatClient.Builder geminiChatClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel gemini){
        return ChatClient.builder(gemini);
    }

    @Bean(name = "ollama")
    public ChatClient.Builder ollamaChatClientBuilder(@Qualifier("ollamaChatModel") ChatModel ollama){
        return ChatClient.builder(ollama);
    }
}
