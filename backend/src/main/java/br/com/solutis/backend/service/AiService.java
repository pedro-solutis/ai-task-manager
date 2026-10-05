package br.com.solutis.backend.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import br.com.solutis.backend.adapter.ai.AiModelAdapter;
import br.com.solutis.backend.dto.TaskAnalysisDTO;
import br.com.solutis.backend.dto.TaskAiRequestDTO;
import br.com.solutis.backend.dto.TaskEnhancedResponseDTO;
import br.com.solutis.backend.exception.AiResponseParsingException;

@Service 
@RequiredArgsConstructor 
public class AiService {

    private final AiModelAdapter aiModelAdapter;

    public TaskEnhancedResponseDTO enhanceTask(TaskAiRequestDTO taskAiRequestDTO) {
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
            taskAiRequestDTO.title(),
            taskAiRequestDTO.description()
        );
        String result = aiModelAdapter.generateText(prompt);
        TaskEnhancedResponseDTO response = formatEnhancedResponse(result, TaskEnhancedResponseDTO.class);
        return response;
    }

    public TaskAnalysisDTO analyzeTask(TaskAiRequestDTO taskRequestDTO) {
        String prompt = String.format(
            "Analyze the following task.\n"+
            "Title: %s\n" +
            "Description: %s\n" +
            "Considering the title and description, provide a detailed analysis of the task."+
            "The analysis should include the following information:\n" +
            "1. Priority: rate the priority of the task (LOW, MEDIUM, HIGH)\n"+
            "2. Complexity: rate the complexity of the task (LOW, MEDIUM, HIGH)\n" +
            "3. Estimated Hours: provide an estimated time to complete the task in whole hours (integer)\n" +
            "4. Analysis Reason: provide a brief explanation for the priority, complexity, and estimated time.\n" +
            "The reason must be in Portuguese and should be concise and directly related to the task's requirements.\n" +
            "CRITICAL: You MUST return the result EXCLUSIVELY as a raw JSON object containing exactly four keys: \"priority\", \"complexity\", \"estimatedHours\", and \"analysisReason\". " +
            "Do NOT wrap the JSON in markdown blocks (like ```json), just return the pure JSON.",
            taskRequestDTO.title(),
            taskRequestDTO.description()
        );

        String result = aiModelAdapter.generateText(prompt);
        TaskAnalysisDTO response = formatEnhancedResponse(result, TaskAnalysisDTO.class);
        return response;
    }

    private <T> T formatEnhancedResponse(String result, Class<T> targetClass) {
        String cleanedResult = result.replaceAll("(?s)^```json\\s*", "")
                                     .replaceAll("(?s)^```\\s*", "")
                                     .replaceAll("(?s)\\s*```$", "")
                                     .trim();
                                             
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(cleanedResult, targetClass);
        } catch (Exception e) {
            throw new AiResponseParsingException("Failed to parse AI response: " + e.getMessage() + ".\n Response received: " + cleanedResult);
        }
    }
}
