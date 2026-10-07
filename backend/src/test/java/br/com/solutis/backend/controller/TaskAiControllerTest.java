package br.com.solutis.backend.controller;

import br.com.solutis.backend.dto.request.TaskEnhanceRequestDTO;
import br.com.solutis.backend.dto.response.TaskEnhancedResponseDTO;
import br.com.solutis.backend.facade.ai.TaskAiFacade;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskAiController.class)
class TaskAiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean 
    private TaskAiFacade taskAiFacade;

    @Test
    @DisplayName("Should call enhance preview")
    void shouldEnhancePreview() throws Exception {
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO("T", "D");
        TaskEnhancedResponseDTO response = new TaskEnhancedResponseDTO("New T", "New D");

        when(taskAiFacade.enhancePreview(any(TaskEnhanceRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/ai/tasks/enhance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New T"))
                .andExpect(jsonPath("$.description").value("New D"));
    }

    @Test
    @DisplayName("Should call enhance by id")
    void shouldEnhanceById() throws Exception {
        UUID id = UUID.randomUUID();
        TaskEnhancedResponseDTO response = new TaskEnhancedResponseDTO("New T", "New D");

        when(taskAiFacade.enhanceById(id)).thenReturn(response);

        mockMvc.perform(post("/ai/tasks/{id}/enhance", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New T"));
    }
}

