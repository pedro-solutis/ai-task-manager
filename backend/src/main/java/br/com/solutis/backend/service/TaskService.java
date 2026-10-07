package br.com.solutis.backend.service;

import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.exception.TaskNotFoundException;
import br.com.solutis.backend.repository.TaskRepository;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return mapToResponse(task);
    }

    @Transactional
    public TaskResponseDTO create(TaskCreationDTO dto) {
        TaskPriority priority = dto.priority() != null ? TaskPriority.valueOf(dto.priority().toUpperCase()) : null;
        Task task = new Task(dto.title(), dto.description(), priority, dto.dueDate());
        
        if (dto.parentTaskId() != null) {
            Task parent = taskRepository.findById(dto.parentTaskId())
                    .orElseThrow(() -> new TaskNotFoundException("Parent task not found with id: " + dto.parentTaskId()));
            task.assignParent(parent);
            cascadeBottomUp(task);
        }

        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    @Transactional
    public TaskResponseDTO update(UUID id, TaskUpdateDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        TaskPriority priority = dto.priority() != null ? TaskPriority.valueOf(dto.priority().toUpperCase()) : null;
        task.updateDetails(dto.title(), dto.description(), priority, dto.dueDate());

        cascadeBottomUp(task);
        cascadeTopDown(task);

        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    @Transactional
    public void delete(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        Task parent = task.getParentTask();
        
        taskRepository.delete(task);

        if (parent != null) {
            parent.getSubTasks().remove(task);
            evaluateParentStatus(parent);
        }
    }
    
    @Transactional
    public TaskResponseDTO updateStatus(UUID id, TaskStatusUpdateDTO status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        TaskStatus newStatus = status.status() != null ? TaskStatus.valueOf(status.status().toUpperCase()) : null;
        if (newStatus != null) {
            task.updateStatus(newStatus);
            cascadeBottomUp(task);
            cascadeTopDown(task);
        }
        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    @Transactional
    public TaskResponseDTO updateParent(UUID taskId, ParentTaskUpdateDTO dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        Task oldParent = task.getParentTask();

        if (dto.parentTaskId() != null) {
            Task newParent = taskRepository.findById(UUID.fromString(dto.parentTaskId()))
                    .orElseThrow(() -> new TaskNotFoundException("Parent task not found with id: " + dto.parentTaskId()));
            task.assignParent(newParent);
            cascadeBottomUp(task);
        } else {
            task.assignParent(null);
        }

        if (oldParent != null && (dto.parentTaskId() == null || !oldParent.getId().equals(UUID.fromString(dto.parentTaskId())))) {
            evaluateParentStatus(oldParent);
        }

        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    @Transactional
    public List<TaskResponseDTO> saveDecomposedTasks(UUID parentTaskId, TaskDecomposeResponseDTO decomposition) {
        Task parentTask = taskRepository.findById(parentTaskId)
                .orElseThrow(() -> new TaskNotFoundException("Parent task not found with id: " + parentTaskId));

        List<Task> subTasksToSave = decomposition.subTasks().stream().map(dto -> {
            TaskPriority priority = dto.priority() != null ? dto.priority() : TaskPriority.MEDIUM;
            LocalDateTime dueDate = dto.dueDate() != null ? dto.dueDate() : parentTask.getDueDate();
            Task subTask = new Task(dto.title(), dto.description(), priority, dueDate);
            subTask.assignParent(parentTask);
            return subTask;
        }).toList();

        for (Task subTask : subTasksToSave) {
            cascadeBottomUp(subTask);
        }

        return taskRepository.saveAll(subTasksToSave).stream().map(this::mapToResponse).toList();
    }

    private void cascadeBottomUp(Task task) {
        Task parent = task.getParentTask();
        if (parent == null) return;

        boolean changed = false;

        if (task.getStatus() == TaskStatus.IN_PROGRESS && parent.getStatus() == TaskStatus.TODO) {
            parent.updateCascadeStatus(TaskStatus.IN_PROGRESS);
            changed = true;
        }

        if (task.getStatus() != TaskStatus.DONE && parent.getStatus() == TaskStatus.DONE) {
            parent.updateCascadeStatus(TaskStatus.IN_PROGRESS);
            changed = true;
        }

        if (task.getDueDate() != null && parent.getDueDate() != null && task.getDueDate().isAfter(parent.getDueDate())) {
            parent.updateDueDate(task.getDueDate());
            changed = true;
        }

        if (task.getStatus() == TaskStatus.DONE && parent.getStatus() != TaskStatus.DONE) {
            if (!parent.getSubTasks().isEmpty()) {
                boolean allChildrenDone = parent.getSubTasks().stream()
                        .allMatch(child -> child.getStatus() == TaskStatus.DONE);
                if (allChildrenDone) {
                    parent.updateCascadeStatus(TaskStatus.DONE);
                    changed = true;
                }
            }
        }

        if (changed) {
            cascadeBottomUp(parent);
        }
    }

    private void cascadeTopDown(Task task) {
        if (task.getSubTasks() == null || task.getSubTasks().isEmpty()) return;

        for (Task child : task.getSubTasks()) {
            boolean changed = false;

            if (task.getStatus() == TaskStatus.DONE && child.getStatus() != TaskStatus.DONE) {
                child.updateCascadeStatus(TaskStatus.DONE);
                changed = true;
            }

            if (task.getStatus() == TaskStatus.TODO && child.getStatus() == TaskStatus.IN_PROGRESS) {
                child.updateCascadeStatus(TaskStatus.TODO);
                changed = true;
            }

            if (child.getDueDate() != null && task.getDueDate() != null && child.getDueDate().isAfter(task.getDueDate())) {
                child.updateDueDate(task.getDueDate());
                changed = true;
            }

            if (changed) {
                cascadeTopDown(child);
            }
        }
    }

    private void evaluateParentStatus(Task parent) {
        if (parent == null || parent.getStatus() == TaskStatus.DONE) return;

        if (!parent.getSubTasks().isEmpty()) {
            boolean allChildrenDone = parent.getSubTasks().stream()
                    .allMatch(child -> child.getStatus() == TaskStatus.DONE);
            if (allChildrenDone) {
                parent.updateCascadeStatus(TaskStatus.DONE);
                cascadeBottomUp(parent);
            }
        }
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
