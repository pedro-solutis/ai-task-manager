package br.com.solutis.backend.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import br.com.solutis.backend.adapter.ai.AiModelAdapter;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.exception.AiResponseParsingException;

@Service 
@RequiredArgsConstructor 
public class AiService {

    private final AiModelAdapter aiModelAdapter;
    private final ObjectMapper objectMapper;

    @Value("${ai.prompts.enhance}")
    private String enhancePromptTemplate;

    @Value("${ai.prompts.analyze}")
    private String analyzePromptTemplate;

    @Value("${ai.prompts.decompose}")
    private String decomposePromptTemplate;

    public TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO taskRequestDTO) {
        String prompt = enhancePromptTemplate.formatted(
                taskRequestDTO.title(),
                taskRequestDTO.description()
        );
        String result = aiModelAdapter.generateText(prompt);
        TaskEnhancedResponseDTO response = formatResponse(result, TaskEnhancedResponseDTO.class);
        return response;
    }

    public TaskAnalysisDTO analyzeTask(TaskAnalysisRequestDTO taskRequestDTO) {
        String prompt = analyzePromptTemplate.formatted(
                LocalDateTime.now(),
                taskRequestDTO.dueDate(),
                taskRequestDTO.priority(),
                taskRequestDTO.title(),
                taskRequestDTO.description()
        );

        String result = aiModelAdapter.generateText(prompt);
        TaskAnalysisDTO response = formatResponse(result, TaskAnalysisDTO.class);
        return response;
    }

    public TaskDecompositionResponseDTO decomposeTask(TaskDecomposeRequestDTO taskRequestDTO) {
        String prompt = decomposePromptTemplate.formatted(
                LocalDateTime.now(),
                taskRequestDTO.dueDate(),
                taskRequestDTO.title(),
                taskRequestDTO.description()
        );
        
        String result = aiModelAdapter.generateText(prompt);
        TaskDecompositionResponseDTO taskDecomposed = formatResponse(result, TaskDecompositionResponseDTO.class);
        return taskDecomposed;
    }

    private <T> T formatResponse(String result, Class<T> targetClass) {
        String cleanedResult = result.trim();
        int startIndex = cleanedResult.indexOf('{');
        int endIndex = cleanedResult.lastIndexOf('}');
        
        if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) {
            cleanedResult = cleanedResult.substring(startIndex, endIndex + 1);
        }
                                             
        try {
            return objectMapper.readValue(cleanedResult, targetClass);
        } catch (Exception e) {
            throw new AiResponseParsingException("Failed to parse AI response: " + e.getMessage() + ".\n Response received: " + cleanedResult);
        }
    }
}
