package br.com.solutis.backend.ai;

import br.com.solutis.backend.dto.request.TaskEnhanceRequestDTO;
import br.com.solutis.backend.dto.response.TaskEnhancedResponseDTO;
import br.com.solutis.backend.exception.AiResponseValidationException;
import br.com.solutis.backend.tools.TaskTools;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.Resource;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringAiTaskServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatMemory chatMemory;

    @Mock
    private TaskTools taskTools;

    @Mock
    private Validator validator;

    @InjectMocks
    private SpringAiTaskService springAiTaskService;

    @BeforeEach
    void setUp() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
    }

    @Test
    @DisplayName("Should enhance task successfully")
    @SuppressWarnings("unchecked")
    void shouldEnhanceTask() {
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO("T", "D");
        TaskEnhancedResponseDTO mockResponse = new TaskEnhancedResponseDTO("Enhanced T", "Enhanced D");

        when(chatClientBuilder.build().prompt()
                .system(any(Resource.class))
                .user(any(Consumer.class))
                .call()
                .entity(TaskEnhancedResponseDTO.class)).thenReturn(mockResponse);

        TaskEnhancedResponseDTO result = springAiTaskService.enhanceTask(request);

        assertThat(result.title()).isEqualTo("Enhanced T");
        assertThat(result.description()).isEqualTo("Enhanced D");
    }

    @Test
    @DisplayName("Should throw AiResponseParsingException when validation fails")
    @SuppressWarnings("unchecked")
    void shouldThrowWhenValidationFails() {
        TaskEnhanceRequestDTO request = new TaskEnhanceRequestDTO("T", "D");
        TaskEnhancedResponseDTO mockResponse = new TaskEnhancedResponseDTO("", "");

        when(chatClientBuilder.build().prompt()
                .system(any(Resource.class))
                .user(any(Consumer.class))
                .call()
                .entity(TaskEnhancedResponseDTO.class)).thenReturn(mockResponse);

        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Title is required");
        when(violation.getPropertyPath()).thenReturn(mock(Path.class));
        when(validator.validate(any())).thenReturn(Set.of(violation));

        assertThatThrownBy(() -> springAiTaskService.enhanceTask(request))
                .isInstanceOf(AiResponseValidationException.class)
                .hasMessageContaining("AI returned invalid data");
    }
}

