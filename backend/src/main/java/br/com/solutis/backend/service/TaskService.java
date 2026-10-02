package br.com.solutis.backend.service;

import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.dto.TaskCreationDTO;
import br.com.solutis.backend.dto.TaskResponseDTO;
import br.com.solutis.backend.dto.TaskStatusUpdateDTO;
import br.com.solutis.backend.dto.TaskUpdateDTO;
import br.com.solutis.backend.exception.TaskNotFoundException;
import br.com.solutis.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Task task = new Task(dto.title(), dto.description(), dto.status(), dto.priority(), dto.dueDate());
        
        if (dto.parentTaskId() != null) {
            Task parent = taskRepository.findById(dto.parentTaskId())
                    .orElseThrow(() -> new TaskNotFoundException("Parent task not found with id: " + dto.parentTaskId()));
            task.assignParent(parent);
        }

        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    @Transactional
    public TaskResponseDTO update(UUID id, TaskUpdateDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        task.updateDetails(dto.title(), dto.description(), dto.priority(), dto.dueDate());
        
        if (dto.parentTaskId() != null) {
            Task parent = taskRepository.findById(dto.parentTaskId())
                    .orElseThrow(() -> new TaskNotFoundException("Parent task not found with id: " + dto.parentTaskId()));
            task.assignParent(parent);
        }
        
        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    @Transactional
    public void delete(UUID id) {
        if(!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
    
    @Transactional
    public TaskResponseDTO updateStatus(UUID id, TaskStatusUpdateDTO status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        task.updateStatus(status.status());
        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    private TaskResponseDTO mapToResponse(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getParentTask() != null ? task.getParentTask().getId() : null
        );
    }
}
