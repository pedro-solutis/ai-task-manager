package br.com.solutis.backend.strategy.ai;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.solutis.backend.dto.request.ChatRequestDTO;
import br.com.solutis.backend.dto.request.TaskAnalysisRequestDTO;
import br.com.solutis.backend.dto.request.TaskDecomposeRequestDTO;
import br.com.solutis.backend.dto.request.TaskEnhanceRequestDTO;
import br.com.solutis.backend.dto.response.ChatResponseDTO;
import br.com.solutis.backend.dto.response.TaskAnalysisResponseDTO;
import br.com.solutis.backend.dto.response.TaskDecomposeResponseDTO;
import br.com.solutis.backend.dto.response.TaskEnhancedResponseDTO;
import br.com.solutis.backend.service.AiProviderService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class ChatClientFactory{

    private final Map<String, ChatClientStrategy> chatClients;
    private final AiProviderService aiProviderService;

    private ChatClientStrategy getActiveChatClient() {
        String provider = aiProviderService.getActiveProvider();
        ChatClientStrategy taskAiService = chatClients.getOrDefault(provider, chatClients.get("gemini"));
        return taskAiService;
    }

    public TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO request) {
        return getActiveChatClient().enhanceTask(request);
    }

    public TaskAnalysisResponseDTO analyzeTask(TaskAnalysisRequestDTO request) {
        return getActiveChatClient().analyzeTask(request);
    }

    public TaskDecomposeResponseDTO decomposeTask(TaskDecomposeRequestDTO request) {
        return getActiveChatClient().decomposeTask(request);
    }

    public ChatResponseDTO chat(ChatRequestDTO request) {
        return getActiveChatClient().chat(request);
    }

}
