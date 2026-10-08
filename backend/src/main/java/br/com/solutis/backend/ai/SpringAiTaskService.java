package br.com.solutis.backend.ai;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
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
import br.com.solutis.backend.exception.AiResponseValidationException;
import br.com.solutis.backend.service.AiProviderService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class SpringAiTaskService implements TaskAiService{

    private final Map<String, ChatClient.Builder> chatClients;
    private final AiProviderService aiProviderService;
    private final ChatMemory chatMemory;
    private final TaskTools taskTools;
    private final Validator validator;

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
    
    private ChatClient getActiveChatClient() {
        String provider = aiProviderService.getActiveProvider();
        ChatClient.Builder builder = chatClients.getOrDefault(provider, chatClients.get("gemini"));
        return builder.build();
    }

    @Override
    public TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO request) {
        TaskEnhancedResponseDTO response = getActiveChatClient().prompt()
            .system(commonGuardrails)
            .user(
                u -> u.text(enhancePrompt)
                .param("title", request.title())
                .param("description", request.description())
            )
            .call()
            .entity(TaskEnhancedResponseDTO.class);
            
        validateResponse(response);
        return response;
    }

    @Override
    public TaskAnalysisResponseDTO analyzeTask(TaskAnalysisRequestDTO request) {
        TaskAnalysisResponseDTO response = getActiveChatClient().prompt()
            .system(commonGuardrails)
            .user(
                u -> u.text(analyzePrompt)
                .param("currentTimestamp",LocalDateTime.now())
                .param("dueDate", request.dueDate())
                .param("currentPriority", request.priority())
                .param("title", request.title())
                .param("description", request.description())
            )
            .call()
            .entity(TaskAnalysisResponseDTO.class);
            
        validateResponse(response);
        return response;
    }

    @Override
    public TaskDecomposeResponseDTO decomposeTask(TaskDecomposeRequestDTO request) {
        TaskDecomposeResponseDTO response = getActiveChatClient().prompt()
            .system(commonGuardrails)
            .user(
                u -> u.text(decomposePrompt)
                .param("title", request.title())
                .param("description", request.description())
                .param("dueDate", request.dueDate())
                .param("currentTimestamp", LocalDateTime.now())
            )
            .call()
            .entity(TaskDecomposeResponseDTO.class);
            
        validateResponse(response);
        return response;
    }

    @Override
    public ChatResponseDTO chat(ChatRequestDTO request) {
        String chatId = request.chatId() != null && !request.chatId().isEmpty() ? request.chatId() : java.util.UUID.randomUUID().toString();
        
        String response = getActiveChatClient().prompt()
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
            .call()
            .content();
            
        return new ChatResponseDTO(chatId, response);
    }
    
    private <T> void validateResponse(T response) {
        if (response == null) {
            throw new AiResponseValidationException("AI returned a null response");
        }
        var violations = validator.validate(response);
        if (!violations.isEmpty()) {
            String messages = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(java.util.stream.Collectors.joining(", "));
            throw new AiResponseValidationException("AI returned invalid data: " + messages);
        }
    }

}
