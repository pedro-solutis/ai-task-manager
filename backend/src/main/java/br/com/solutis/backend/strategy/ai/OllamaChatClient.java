package br.com.solutis.backend.strategy.ai;

import java.time.LocalDateTime;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import br.com.solutis.backend.dto.request.ChatRequestDTO;
import br.com.solutis.backend.dto.request.TaskAnalysisRequestDTO;
import br.com.solutis.backend.dto.request.TaskDecomposeRequestDTO;
import br.com.solutis.backend.dto.request.TaskEnhanceRequestDTO;
import br.com.solutis.backend.dto.response.ChatResponseDTO;
import br.com.solutis.backend.dto.response.TaskAnalysisResponseDTO;
import br.com.solutis.backend.dto.response.TaskDecomposeResponseDTO;
import br.com.solutis.backend.dto.response.TaskEnhancedResponseDTO;
import br.com.solutis.backend.tools.TaskTools;
import jakarta.validation.Validator;

@Component("ollama")
public class OllamaChatClient extends ChatClientStrategy{

    @Value("${app.ai.prompts.enhance}")
    private Resource enhancePrompt;
    @Value("${app.ai.prompts.analyze}")
    private Resource analyzePrompt;
    @Value("${app.ai.prompts.decompose}")
    private Resource decomposePrompt;
    @Value("${app.ai.prompts.assistant}")
    private Resource assistantPrompt;
    @Value ("${app.ai.context.guardrails}")
    private Resource commonGuardrails;

    public OllamaChatClient(@Qualifier("ollamaClient") ChatClient chatClient, ChatMemory chatMemory, TaskTools taskTools, Validator validator) {
        super(chatClient, chatMemory, taskTools, validator);
    }

    @Override
    public String getChatClient() {
        return "ollama";
    }

    @Override
    public TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO request) {
        TaskEnhancedResponseDTO response = chatClient.prompt()
        .user(
            u -> u.text(enhancePrompt)
            .param("title", request.title())
            .param("description", request.description())
        )
        .options(
            OllamaChatOptions.builder()
            .temperature(0.6)
            .repeatPenalty(1.1)
            .numCtx(4096)
        )
        .call()
        .entity(TaskEnhancedResponseDTO.class);
            
        validateResponse(response);
        return response;
    }

    @Override
    public TaskAnalysisResponseDTO analyzeTask(TaskAnalysisRequestDTO request) {
        TaskAnalysisResponseDTO response = chatClient.prompt()
        .user(
            u -> u.text(enhancePrompt)
            .param("title", request.title())
            .param("description", request.description())
        )
        .options(
            OllamaChatOptions.builder()
            .temperature(0.0)
            .repeatPenalty(1.15)
            .numCtx(4096)
        )
        .call()
        .entity(TaskAnalysisResponseDTO.class);
            
        validateResponse(response);
        return response;
    }

    @Override
    public TaskDecomposeResponseDTO decomposeTask(TaskDecomposeRequestDTO request) {
        TaskDecomposeResponseDTO response = chatClient.prompt()
        .system(commonGuardrails)
        .user(
            u -> u.text(decomposePrompt)
            .param("title", request.title())
            .param("description", request.description())
            .param("dueDate", request.dueDate())
            .param("currentTimestamp", LocalDateTime.now())
        )
        .options(
            OllamaChatOptions.builder()
            .temperature(0.1)
            .repeatPenalty(1.1)
            .numCtx(4096)
        )
        .call()
        .entity(TaskDecomposeResponseDTO.class);
        
        validateResponse(response);
        return response;
    }

    @Override
    public ChatResponseDTO chat(ChatRequestDTO request) {
        String chatId = request.chatId() != null && !request.chatId().isEmpty() ? request.chatId() : java.util.UUID.randomUUID().toString();
        
        String response = chatClient.prompt()
            .system(
                s -> s.text(assistantPrompt)
                .param("currentTimestamp", LocalDateTime.now())
                .param("guardrails", commonGuardrails)
            )
            .user(
                request.prompt()
            )
            .tools(taskTools)
            .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
            .advisors(
                a -> a.param(
                    "chat_memory_conversation_id", chatId
                )
            )
            .options(
                OllamaChatOptions.builder()
                .temperature(0.4)
                .numCtx(4096)
            )
            .call()
            .content();
            
        return new ChatResponseDTO(chatId, response);
    }

}
