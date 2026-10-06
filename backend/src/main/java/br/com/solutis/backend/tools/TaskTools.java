package br.com.solutis.backend.tools;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import br.com.solutis.backend.dto.response.TaskResponseDTO;
import br.com.solutis.backend.repository.TaskRepository;
import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.exception.TaskNotFoundException;
import br.com.solutis.backend.domain.enums.*;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class TaskTools {

    private final TaskRepository taskRepository;

    @Tool (description = "Search for pending tasks finding by status not equal DONE")
    public List<TaskResponseDTO> findPendingTasks(){
        return taskRepository.findPendingTask()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Tool (description = "Search for task filtering by status, priority and due date. The dueDate must be in ISO-8601 format (YYYY-MM-DDTHH:mm:ss)")
    public List<TaskResponseDTO> filterTask(String status, String priority, String dueDate){
        TaskStatus taskStatus = status != null && !status.isBlank() ? TaskStatus.valueOf(status.toUpperCase()) : null;
        TaskPriority taskPriority = priority != null && !priority.isBlank() ? TaskPriority.valueOf(priority.toUpperCase()) : null;
        LocalDateTime date = dueDate != null && !dueDate.isBlank() ? LocalDateTime.parse(dueDate) : null;
        
        return taskRepository.filterTask(taskStatus, taskPriority, date)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Tool (description = "Search for task by id")
    public TaskResponseDTO findById(UUID taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        return mapToResponse(task);
    }

    private TaskResponseDTO mapToResponse(Task task) {
        List<TaskResponseDTO> subTasksDTO = task.getSubTasks() != null
                ? task.getSubTasks().stream().map(this::mapToResponse).toList()
                : List.of();

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getParentTask() != null ? task.getParentTask().getId() : null,
                subTasksDTO
        );
    }
}
