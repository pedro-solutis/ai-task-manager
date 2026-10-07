package br.com.solutis.backend.facade.ai;

import br.com.solutis.backend.ai.TaskAiService;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.dto.request.TaskAnalysisRequestDTO;
import br.com.solutis.backend.dto.request.TaskDecomposeRequestDTO;
import br.com.solutis.backend.dto.request.TaskEnhanceRequestDTO;
import br.com.solutis.backend.dto.response.*;
import br.com.solutis.backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAiFacadeTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskAiService taskAiService;

    @InjectMocks
    private TaskAiFacade taskAiFacade;

    private UUID taskId;
    private TaskResponseDTO mockTaskResponse;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        mockTaskResponse = new TaskResponseDTO(
                taskId,
                "Test Title",
                "Test Desc",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                List.of()
        );
    }

    @Test
    @DisplayName("Should enhance task by id successfully")
    void shouldEnhanceById() {
        TaskEnhancedResponseDTO enhancedDTO = new TaskEnhancedResponseDTO("New Title", "New Desc");
        when(taskService.findById(taskId)).thenReturn(mockTaskResponse);
        when(taskAiService.enhanceTask(any(TaskEnhanceRequestDTO.class))).thenReturn(enhancedDTO);

        TaskEnhancedResponseDTO result = taskAiFacade.enhanceById(taskId);

        assertThat(result.title()).isEqualTo("New Title");
        verify(taskAiService, times(1)).enhanceTask(any(TaskEnhanceRequestDTO.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to enhance DONE task")
    void shouldThrowWhenEnhancingDoneTask() {
        TaskResponseDTO doneTask = new TaskResponseDTO(taskId, "T", "D", TaskStatus.DONE, TaskPriority.LOW, LocalDateTime.now(), null, null, null, List.of());
        when(taskService.findById(taskId)).thenReturn(doneTask);

        assertThatThrownBy(() -> taskAiFacade.enhanceById(taskId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Can not enhance a DONE task.");
    }

    @Test
    @DisplayName("Should analyze task by id successfully")
    void shouldAnalyzeById() {
        TaskAnalysisResponseDTO analysisDTO = new TaskAnalysisResponseDTO(TaskPriority.HIGH, br.com.solutis.backend.domain.enums.TaskComplexity.HIGH, 5, "Reasoning");
        when(taskService.findById(taskId)).thenReturn(mockTaskResponse);
        when(taskAiService.analyzeTask(any(TaskAnalysisRequestDTO.class))).thenReturn(analysisDTO);

        TaskAnalysisResponseDTO result = taskAiFacade.analyzeById(taskId);

        assertThat(result.priority()).isEqualTo(TaskPriority.HIGH);
        verify(taskAiService, times(1)).analyzeTask(any(TaskAnalysisRequestDTO.class));
    }

    @Test
    @DisplayName("Should decompose task by id successfully")
    void shouldDecomposeById() {
        TaskDecomposeResponseDTO decomposeDTO = new TaskDecomposeResponseDTO(List.of(
                new SubTaskDTO("Sub 1", "Desc", TaskPriority.LOW, LocalDateTime.now().plusDays(1))
        ));
        
        when(taskService.findById(taskId)).thenReturn(mockTaskResponse);
        when(taskAiService.decomposeTask(any(TaskDecomposeRequestDTO.class))).thenReturn(decomposeDTO);
        
        TaskResponseDTO subTaskResponse = new TaskResponseDTO(UUID.randomUUID(), "Sub 1", "Desc", TaskStatus.TODO, TaskPriority.LOW, LocalDateTime.now(), null, null, taskId, List.of());
        when(taskService.saveDecomposedTasks(eq(taskId), eq(decomposeDTO))).thenReturn(List.of(subTaskResponse));

        List<TaskResponseDTO> result = taskAiFacade.decomposeById(taskId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().title()).isEqualTo("Sub 1");
        verify(taskService, times(1)).saveDecomposedTasks(taskId, decomposeDTO);
    }
}

