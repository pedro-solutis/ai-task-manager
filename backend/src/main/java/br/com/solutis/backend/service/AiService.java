package br.com.solutis.backend.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import br.com.solutis.backend.adapter.ai.AiModelAdapter;
import br.com.solutis.backend.dto.TaskEnhancedRequestDTO;
import br.com.solutis.backend.dto.TaskEnhancedResponseDTO;
import br.com.solutis.backend.exception.AiResponseParsingException;

@Service 
@RequiredArgsConstructor 
public class AiService {

    private final AiModelAdapter aiModelAdapter;

    public TaskEnhancedResponseDTO enhanceTask(TaskEnhancedRequestDTO taskEnhancedRequestDTO) {
        String prompt = String.format(
            "Consider the following task.\n" +
            "Title: %s\n" +
            "Description: %s\n" +
            "Enhance the description of the task to make it more detailed and engaging. " +
            "The new description should be more detailed and engaging, providing clear instructions and the context of the task. " +
            "The description should be clear, concise, and easy to understand, providing all the necessary information for the task to be completed successfully. " +
            "Must be in Portuguese and use a professional tone, using technical terms when necessary. " +
            "Do not change the title, only enhance the description.\n\n" +
            "CRITICAL: You MUST return the result EXCLUSIVELY as a raw JSON object containing exactly two keys: \"title\" and \"description\". " +
            "Do NOT wrap the JSON in markdown blocks (like ```json), just return the pure JSON.",
            taskEnhancedRequestDTO.title(),
            taskEnhancedRequestDTO.description()
        );
        String result = aiModelAdapter.generateText(prompt);
        return formatEnhancedResponse(result);
    }

    private TaskEnhancedResponseDTO formatEnhancedResponse(String result) {
        String cleanedResult = result.replaceAll("(?s)^```json\\s*", "")
                                     .replaceAll("(?s)^```\\s*", "")
                                     .replaceAll("(?s)\\s*```$", "")
                                     .trim();
                                             
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(cleanedResult, TaskEnhancedResponseDTO.class);
        } catch (Exception e) {
            throw new AiResponseParsingException("Failed to parse AI response: " + e.getMessage() + ".\n Response received: " + cleanedResult);
        }
    }

}
