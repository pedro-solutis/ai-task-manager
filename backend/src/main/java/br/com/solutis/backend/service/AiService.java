package br.com.solutis.backend.service;

import java.time.LocalDateTime;

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

    public TaskEnhancedResponseDTO enhanceTask(TaskEnhanceRequestDTO taskRequestDTO) {
        String prompt = """
        Refine and enhance the provided task title and description into a clear, concise, and professional version.

        Rules:
        1. Language: Generate the output `title` and `description` in the exact same language used in the input.
        2. Scope & Clarity:
           - `title`: Keep it concise, actionable, and aligned with the enhanced description.
           - `description`: Rewrite into a brief, direct, and professional scope of work. Avoid fluff, conversational fillers, or excessive verbosity.
        3. Return a valid JSON object strictly adhering to the schema with properties `title` and `description`.
        4. Output pure JSON only. Do not include markdown formatting, backticks, or comments.

        Input:
        - Title: %s
        - Description: %s
        """.formatted(
                taskRequestDTO.title(),
                taskRequestDTO.description()
        );
        String result = aiModelAdapter.generateText(prompt);
        TaskEnhancedResponseDTO response = formatEnhancedResponse(result, TaskEnhancedResponseDTO.class);
        return response;
    }

    public TaskAnalysisDTO analyzeTask(TaskAnalysisRequestDTO taskRequestDTO) {
        String prompt = """
        Analyze the provided task based on its title, description, and due date to evaluate priority, complexity, required effort, and rationale.

        Rules:
        1. Language: Generate the `analysisReason` in the exact same language used in the input title and description.
        2. Field Specifications:
           - `priority`: Recommended priority matching TaskPriority enum: "LOW", "MEDIUM" or "HIGH". Evaluate urgency based on the due date and scope impact.
           - `complexity`: Complexity assessment matching TaskComplexity enum: "LOW", "MEDIUM" or "HIGH". Base this on technical scope, unknowns, and potential dependencies described in the task.
           - `estimatedHours`: Integer representing the estimated total hours required to complete the task. Must be greater than 0.
           - `analysisReason`: Concise, professional justification explaining why the chosen priority, complexity, and estimated hours were assigned.
        3. Return a valid JSON object strictly adhering to the schema with properties `priority`, `complexity`, `estimatedHours`, and `analysisReason`.
        4. Output pure JSON only. Do not include markdown formatting, backticks, or comments.

        Input:
        - Current Timestamp: %s
        - Title: %s
        - Description: %s
        - Due Date: %s
        - Current Priority: %s
        """.formatted(
                LocalDateTime.now(),
                taskRequestDTO.title(),
                taskRequestDTO.description(),
                taskRequestDTO.dueDate(),
                taskRequestDTO.priority()
        );

        String result = aiModelAdapter.generateText(prompt);
        TaskAnalysisDTO response = formatEnhancedResponse(result, TaskAnalysisDTO.class);
        return response;
    }

    public TaskDecompositionResponseDTO decomposeTask(TaskDecomposeRequestDTO taskRequestDTO) {
        String prompt = """
        Decompose the provided task into actionable subtasks with incremental milestone deadlines.

        Rules:
        1. Break down the input task into sequential, concrete subtasks ordered by execution dependency.
        2. Language: Generate the `title` and `description` of all subtasks in the exact same language used in the input title and description.
        3. Return a valid JSON object strictly adhering to the schema with the property `subTasks`.
        4. For each subtask, populate:
           - `title`: Short and imperative title.
           - `description`: Clear specification of the deliverable.
           - `priority`: One of the enum values: "LOW", "MEDIUM" or "HIGH".
           - `dueDate`: ISO-8601 formatted date-time string ("YYYY-MM-DDTHH:mm:ss").
        5. Scheduling & Date constraints:
           - Distribute subtask due dates progressively across the timeline between Current Timestamp and Parent Due Date.
           - Estimate the effort/complexity required for each step and assign intermediate checkpoints accordingly.
           - Earlier subtasks MUST have earlier deadlines (strictly ascending order: dueDate_1 < dueDate_2 < ... <= parent dueDate).
           - DO NOT set every subtask's dueDate to the parent task's final dueDate. Only the very last subtask may match the parent Due Date.
           - All due dates must fall strictly after Current Timestamp and on or before Parent Due Date.
        6. Output pure JSON only. Do not include markdown formatting, backticks, or conversational text. 

        Input:
        - Current Timestamp: %s
        - Title: %s
        - Description: %s
        - Due Date: %s
        """.formatted(
                LocalDateTime.now(),
                taskRequestDTO.title(),
                taskRequestDTO.description(),
                taskRequestDTO.dueDate()
        );
        
        String result = aiModelAdapter.generateText(prompt);
        TaskDecompositionResponseDTO taskDecomposed = formatEnhancedResponse(result, TaskDecompositionResponseDTO.class);
        return taskDecomposed;
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
