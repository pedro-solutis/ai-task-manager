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

    @Tool(description = "Search and filter tasks. All parameters are optional. " +
            "- taskId: use to find a specific task by its ID. " +
            "- status: filter by task status (TODO, IN_PROGRESS, DONE). " +
            "- priority: filter by task priority (LOW, MEDIUM, HIGH). " +
            "- maxDueDate: maximum due date in ISO-8601 format (YYYY-MM-DDTHH:mm:ss). " +
            "- pendingOnly: if 'true', returns only tasks that do NOT have the DONE status.")
    public List<TaskResponseDTO> searchTasks(
            String taskId,
            String status,
            String priority,
            String maxDueDate,
            Boolean pendingOnly) {

        if (taskId != null && !taskId.isBlank()) {
            return taskRepository.findById(UUID.fromString(taskId))
                    .map(this::mapToResponse)
                    .map(List::of)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        }

        TaskStatus taskStatus = status != null && !status.isBlank() ? TaskStatus.valueOf(status.toUpperCase()) : null;
        TaskPriority taskPriority = priority != null && !priority.isBlank() ? TaskPriority.valueOf(priority.toUpperCase()) : null;
        LocalDateTime date = maxDueDate != null && !maxDueDate.isBlank() ? LocalDateTime.parse(maxDueDate) : null;
        boolean excludeDone = Boolean.TRUE.equals(pendingOnly);

        return taskRepository.searchTasks(taskStatus, taskPriority, date, excludeDone)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Tool(description = "Count the number of tasks based on filters. All parameters are optional. " +
            "- status: filter by task status (TODO, IN_PROGRESS, DONE). " +
            "- priority: filter by task priority (LOW, MEDIUM, HIGH). " +
            "- maxDueDate: maximum due date in ISO-8601 format (YYYY-MM-DDTHH:mm:ss). " +
            "- pendingOnly: if 'true', counts only tasks that do NOT have the DONE status.")
    public long countTasks(
            String status,
            String priority,
            String maxDueDate,
            Boolean pendingOnly) {

        TaskStatus taskStatus = status != null && !status.isBlank() ? TaskStatus.valueOf(status.toUpperCase()) : null;
        TaskPriority taskPriority = priority != null && !priority.isBlank() ? TaskPriority.valueOf(priority.toUpperCase()) : null;
        LocalDateTime date = maxDueDate != null && !maxDueDate.isBlank() ? LocalDateTime.parse(maxDueDate) : null;
        boolean excludeDone = Boolean.TRUE.equals(pendingOnly);

        return taskRepository.countTasks(taskStatus, taskPriority, date, excludeDone);
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
