package br.com.solutis.backend.strategy.ai;

import br.com.solutis.backend.dto.request.TaskEnhanceRequestDTO;
import br.com.solutis.backend.dto.response.TaskEnhancedResponseDTO;
import br.com.solutis.backend.exception.AiResponseValidationException;
import br.com.solutis.backend.service.AiProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatClientFactoryTest {

    @Mock
    private ChatClientStrategy taskAiService;

    @Mock
    private Map<String, ChatClientStrategy> chatClients;

    @Mock
    private AiProviderService aiProviderService;

    @InjectMocks
    private ChatClientFactory springAiTaskService;

    @BeforeEach
    void setUp() {
        when(aiProviderService.getActiveProvider()).thenReturn("gemini");
        when(chatClients.getOrDefault(any(), any())).thenReturn(taskAiService);
    }

    @Test
    @DisplayName("Should enhance task successfully")
    void shouldEnhanceTask() {
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO("T", "D");
        TaskEnhancedResponseDTO mockResponse = new TaskEnhancedResponseDTO("Enhanced T", "Enhanced D");

        when(taskAiService.enhanceTask(request)).thenReturn(mockResponse);

        TaskEnhancedResponseDTO result = springAiTaskService.enhanceTask(request);

        assertThat(result.title()).isEqualTo("Enhanced T");
        assertThat(result.description()).isEqualTo("Enhanced D");
    }

    @Test
    @DisplayName("Should throw AiResponseValidationException when validation fails")
    void shouldThrowWhenValidationFails() {
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO("T", "D");

        when(taskAiService.enhanceTask(request)).thenThrow(new AiResponseValidationException("AI returned invalid data: Title is required"));

        assertThatThrownBy(() -> springAiTaskService.enhanceTask(request))
                .isInstanceOf(AiResponseValidationException.class)
                .hasMessageContaining("AI returned invalid data");
    }
}

