package br.com.solutis.backend.facade.ai;

import br.com.solutis.backend.service.*;
import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.domain.enums.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class TaskAiFacade {

    private final TaskService taskService;
    private final AiService aiService;    

    @Transactional(readOnly = true)
    public TaskEnhancedResponseDTO enhanceById(UUID taskId){
        TaskResponseDTO task = taskService.findById(taskId);
        if (task.status() == TaskStatus.DONE)
            throw new IllegalStateException("Can not enhance a DONE task.");
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO(
            task.title(),
            task.description()
        );
        return aiService.enhanceTask(request);
        
    }

    @Transactional (readOnly = true)
    public TaskAnalysisDTO analyzeById(UUID taskId){
        TaskResponseDTO task = taskService.findById(taskId);
        TaskAnalysisRequestDTO request = new TaskAnalysisRequestDTO(
            task.title(),
            task.description(),
            task.priority().toString(),
            task.dueDate()
        );
        return aiService.analyzeTask(request);
    }

    @Transactional 
    public List<TaskResponseDTO> decomposeById(UUID taskId){
        TaskResponseDTO parent = taskService.findById(taskId);
        if(parent.status() == TaskStatus.DONE)
            throw new IllegalStateException("Can not decompose a DONE task.");
        TaskDecomposeRequestDTO request = new TaskDecomposeRequestDTO(
            parent.title(),
            parent.description(),
            parent.dueDate()
        );
        TaskDecompositionResponseDTO decomposition = aiService.decomposeTask(request);
        return taskService.saveDecomposedTasks(taskId, decomposition);
    }

    public TaskEnhancedResponseDTO enhancePreview(TaskEnhanceRequestDTO request){
        return aiService.enhanceTask(request);
    }

    public TaskAnalysisDTO analyzePreview(TaskAnalysisRequestDTO request){
        return aiService.analyzeTask(request);
    }

    public TaskDecompositionResponseDTO decomposePreview(TaskDecomposeRequestDTO request){
        return aiService.decomposeTask(request);
    }

}
