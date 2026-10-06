package br.com.solutis.backend.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import br.com.solutis.backend.adapter.ai.AiModelAdapter;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.exception.AiResponseParsingException;

@Service 
@RequiredArgsConstructor 
public class AiService {

    private final AiModelAdapter aiModelAdapter;

    @Value("${ai.prompts.enhance}")
    private String enhancePromptTemplate;

    @Value("${ai.prompts.analyze}")
    private String analyzePromptTemplate;

    @Value("${ai.prompts.decompose}")
    private String decomposePromptTemplate;

    public TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO taskRequestDTO) {
        var converter = new BeanOutputConverter<>(TaskEnhancedResponseDTO.class);
        var template = new PromptTemplate(enhancePromptTemplate);
        
        String prompt = template.render(Map.of(
                "title", taskRequestDTO.title() != null ? taskRequestDTO.title() : "",
                "description", taskRequestDTO.description() != null ? taskRequestDTO.description() : "",
                "formatInstructions", converter.getFormat()
        ));
        
        String result = aiModelAdapter.generateText(prompt);
        
        try {
            return converter.convert(result);
        } catch (Exception e) {
            throw new AiResponseParsingException("Failed to parse AI response: " + e.getMessage());
        }
    }

    public TaskAnalysisDTO analyzeTask(TaskAnalysisRequestDTO taskRequestDTO) {
        var converter = new BeanOutputConverter<>(TaskAnalysisDTO.class);
        var template = new PromptTemplate(analyzePromptTemplate);
        
        String prompt = template.render(Map.of(
                "currentTimestamp", LocalDateTime.now().toString(),
                "dueDate", taskRequestDTO.dueDate() != null ? taskRequestDTO.dueDate().toString() : "Not defined",
                "currentPriority", taskRequestDTO.priority() != null ? taskRequestDTO.priority().toString() : "Not defined",
                "title", taskRequestDTO.title() != null ? taskRequestDTO.title() : "",
                "description", taskRequestDTO.description() != null ? taskRequestDTO.description() : "",
                "formatInstructions", converter.getFormat()
        ));

        String result = aiModelAdapter.generateText(prompt);
        
        try {
            return converter.convert(result);
        } catch (Exception e) {
            throw new AiResponseParsingException("Failed to parse AI response: " + e.getMessage());
        }
    }

    public TaskDecompositionResponseDTO decomposeTask(TaskDecomposeRequestDTO taskRequestDTO) {
        var converter = new BeanOutputConverter<>(TaskDecompositionResponseDTO.class);
        var template = new PromptTemplate(decomposePromptTemplate);
        
        String prompt = template.render(Map.of(
                "currentTimestamp", LocalDateTime.now().toString(),
                "dueDate", taskRequestDTO.dueDate() != null ? taskRequestDTO.dueDate().toString() : "Not defined",
                "title", taskRequestDTO.title() != null ? taskRequestDTO.title() : "",
                "description", taskRequestDTO.description() != null ? taskRequestDTO.description() : "",
                "formatInstructions", converter.getFormat()
        ));
        
        String result = aiModelAdapter.generateText(prompt);
        
        try {
            return converter.convert(result);
        } catch (Exception e) {
            throw new AiResponseParsingException("Failed to parse AI response: " + e.getMessage());
        }
    }
}
