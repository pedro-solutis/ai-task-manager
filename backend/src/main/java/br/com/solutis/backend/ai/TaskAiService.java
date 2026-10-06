package br.com.solutis.backend.ai;

import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.dto.request.*;

public interface TaskAiService {
    TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO request);
    TaskAnalysisResponseDTO analyzeTask(TaskAnalysisRequestDTO request);
    TaskDecomposeResponseDTO decomposeTask(TaskDecomposeRequestDTO request);
    ChatResponseDTO chat(ChatRequestDTO request);
}
