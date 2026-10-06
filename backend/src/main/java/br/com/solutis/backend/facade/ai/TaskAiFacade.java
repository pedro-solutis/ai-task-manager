package br.com.solutis.backend.facade.ai;

import br.com.solutis.backend.service.*;
import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.domain.enums.*;
import br.com.solutis.backend.ai.TaskAiService;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class TaskAiFacade {

    private final TaskService taskService;
    private final TaskAiService taskAiService;    

    @Transactional(readOnly = true)
    public TaskEnhancedResponseDTO enhanceById(UUID taskId){
        TaskResponseDTO task = taskService.findById(taskId);
        if (task.status() == TaskStatus.DONE)
            throw new IllegalStateException("Can not enhance a DONE task.");
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO(
            task.title(),
            task.description()
        );
        return taskAiService.enhanceTask(request);
        
    }

    @Transactional (readOnly = true)
    public TaskAnalysisResponseDTO analyzeById(UUID taskId){
        TaskResponseDTO task = taskService.findById(taskId);
        TaskAnalysisRequestDTO request = new TaskAnalysisRequestDTO(
            task.title(),
            task.description(),
            task.priority().toString(),
            task.dueDate()
        );
        return taskAiService.analyzeTask(request);
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
        TaskDecomposeResponseDTO decomposition = taskAiService.decomposeTask(request);
        return taskService.saveDecomposedTasks(taskId, decomposition);
    }

    public TaskEnhancedResponseDTO enhancePreview(TaskEnhanceRequestDTO request){
        return taskAiService.enhanceTask(request);
    }

    public TaskAnalysisResponseDTO analyzePreview(TaskAnalysisRequestDTO request){
        return taskAiService.analyzeTask(request);
    }

    public TaskDecomposeResponseDTO decomposePreview(TaskDecomposeRequestDTO request){
        return taskAiService.decomposeTask(request);
    }

}
