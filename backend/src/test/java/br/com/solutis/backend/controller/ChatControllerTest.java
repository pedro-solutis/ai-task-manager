package br.com.solutis.backend.controller;

import br.com.solutis.backend.dto.request.ChatRequestDTO;
import br.com.solutis.backend.dto.response.ChatResponseDTO;
import br.com.solutis.backend.service.ChatService;
import br.com.solutis.backend.service.AiProviderService;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean 
    private ChatService chatService;

    @MockitoBean
    private AiProviderService aiProviderService;

    @Test
    @DisplayName("Should process chat request and return response")
    void shouldChat() throws Exception {
        UUID chatId =  UUID.randomUUID();
        String promptRequest = "Hello";
        String chatResponse = "Hi there!";
        ChatRequestDTO request = new ChatRequestDTO(chatId.toString(), promptRequest);
        ChatResponseDTO response = new ChatResponseDTO(chatId.toString(), chatResponse);

        when(chatService.chat(any(ChatRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(chatId.toString()))
                .andExpect(jsonPath("$.chatResponse").value(chatResponse));
    }
}

