package br.com.solutis.backend.controller;

import br.com.solutis.backend.dto.TaskCreationDTO;
import br.com.solutis.backend.dto.TaskResponseDTO;
import br.com.solutis.backend.dto.TaskStatusUpdateDTO;
import br.com.solutis.backend.dto.TaskUpdateDTO;
import br.com.solutis.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> findAll(@PageableDefault(page=0, size=10, sort="dueDate", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable("id") UUID taskId) {
        return ResponseEntity.ok(taskService.findById(taskId));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskCreationDTO dto, UriComponentsBuilder uriBuilder) {
        TaskResponseDTO createdTask = taskService.create(dto);
        URI location = uriBuilder.path("/tasks/{id}").buildAndExpand(createdTask.id()).toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable("id") UUID taskId, @Valid @RequestBody TaskUpdateDTO dto) {
        return ResponseEntity.ok(taskService.update(taskId, dto));
    }
    
    @PatchMapping ("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@PathVariable("id") UUID taskId, @Valid @RequestBody TaskStatusUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateStatus(taskId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }
}
