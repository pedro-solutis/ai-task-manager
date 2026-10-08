package br.com.solutis.backend.controller;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.dto.request.TaskCreationDTO;
import br.com.solutis.backend.dto.response.TaskResponseDTO;
import br.com.solutis.backend.service.TaskService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean 
    private TaskService taskService;

    @Test
    @DisplayName("Should return all tasks")
    void shouldFindAll() throws Exception {
        TaskResponseDTO task = new TaskResponseDTO(UUID.randomUUID(), "T", "D", TaskStatus.TODO, TaskPriority.LOW, LocalDateTime.now().plusDays(1), LocalDateTime.now(), LocalDateTime.now(), null, List.of());
        List<TaskResponseDTO> list = List.of(task);
        
        when(taskService.findAll("dueDate", "ASC")).thenReturn(list);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("T"));
    }

    @Test
    @DisplayName("Should return task by ID")
    void shouldFindById() throws Exception {
        UUID id = UUID.randomUUID();
        TaskResponseDTO task = new TaskResponseDTO(id, "T", "D", TaskStatus.TODO, TaskPriority.LOW, LocalDateTime.now().plusDays(1), LocalDateTime.now(), LocalDateTime.now(), null, List.of());
        
        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @DisplayName("Should create task")
    void shouldCreateTask() throws Exception {
        TaskCreationDTO dto = new TaskCreationDTO("T", "D", "LOW", LocalDateTime.now().plusDays(1), null);
        UUID id = UUID.randomUUID();
        TaskResponseDTO response = new TaskResponseDTO(id, "T", "D", TaskStatus.TODO, TaskPriority.LOW, LocalDateTime.now().plusDays(1), LocalDateTime.now(), LocalDateTime.now(), null, List.of());

        when(taskService.create(any(TaskCreationDTO.class))).thenReturn(response);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/tasks/" + id.toString()))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @DisplayName("Should delete task")
    void shouldDeleteTask() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(taskService).delete(id);

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isNoContent());
    }
}

