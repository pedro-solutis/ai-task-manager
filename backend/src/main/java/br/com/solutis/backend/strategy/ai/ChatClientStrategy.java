package br.com.solutis.backend.strategy.ai;

import br.com.solutis.backend.dto.response.*;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.exception.*;
import br.com.solutis.backend.tools.*;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public abstract class ChatClientStrategy {

    protected  final ChatClient chatClient;
    protected  final ChatMemory chatMemory;
    protected  final TaskTools taskTools;
    protected  final Validator validator;

    public abstract String getChatClient();
    public abstract TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO request);
    public abstract TaskAnalysisResponseDTO analyzeTask(TaskAnalysisRequestDTO request);
    public abstract TaskDecomposeResponseDTO decomposeTask(TaskDecomposeRequestDTO request);
    public abstract ChatResponseDTO chat(ChatRequestDTO request);

    protected  <T> void validateResponse(T response){
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
