package br.com.solutis.backend.controller;

import br.com.solutis.backend.service.TaskService;
import br.com.solutis.backend.dto.request.*;
import br.com.solutis.backend.dto.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tasks")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Find all tasks")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> findAll(
        @RequestParam (defaultValue = "dueDate") String sort,
        @RequestParam (defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(taskService.findAll(sort, direction));
    }

    @Operation(summary = "Find task by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable("id") UUID taskId) {
        return ResponseEntity.ok(taskService.findById(taskId));
    }

    @Operation(summary = "Create task")
    @ApiResponse(responseCode = "201", description = "Successful operation")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskCreationDTO dto, UriComponentsBuilder uriBuilder) {
        TaskResponseDTO createdTask = taskService.create(dto);
        URI location = uriBuilder.path("/tasks/{id}").buildAndExpand(createdTask.id()).toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @Operation(summary = "Update task by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable("id") UUID taskId, @Valid @RequestBody TaskUpdateDTO dto) {
        return ResponseEntity.ok(taskService.update(taskId, dto));
    }
    
    @Operation(summary = "Update task status by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @PatchMapping ("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@PathVariable("id") UUID taskId, @Valid @RequestBody TaskStatusUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateStatus(taskId, dto));
    }

    @Operation(summary = "Update task parent by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @PatchMapping ("/{id}/parent")
    public ResponseEntity<TaskResponseDTO> updateParent(@PathVariable("id") UUID taskId, @RequestBody @Valid ParentTaskUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateParent(taskId, dto));
    }

    @Operation(summary = "Delete task by ID")
    @ApiResponse(responseCode = "204", description = "Successful operation")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }
}
