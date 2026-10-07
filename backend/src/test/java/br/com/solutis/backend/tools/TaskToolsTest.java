package br.com.solutis.backend.tools;

import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.dto.response.TaskResponseDTO;
import br.com.solutis.backend.exception.TaskNotFoundException;
import br.com.solutis.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskToolsTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskTools taskTools;

    private Task task;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = new Task("Task Name", "Desc", TaskPriority.MEDIUM, LocalDateTime.now().plusDays(1));
        try {
            var idField = Task.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(task, taskId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should search task by ID successfully")
    void shouldSearchTaskById() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        List<TaskResponseDTO> results = taskTools.searchTasks(taskId.toString(), null, null, null, null);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().id()).isEqualTo(taskId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).searchTasks(any(), any(), any(), anyBoolean());
    }

    @Test
    @DisplayName("Should throw exception when task ID not found")
    void shouldThrowExceptionWhenTaskIdNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskTools.searchTasks(taskId.toString(), null, null, null, null))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found with id");
    }

    @Test
    @DisplayName("Should search tasks using filters")
    void shouldSearchTasksUsingFilters() {
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        when(taskRepository.searchTasks(TaskStatus.TODO, TaskPriority.HIGH, date, true)).thenReturn(List.of(task));

        List<TaskResponseDTO> results = taskTools.searchTasks(null, "TODO", "HIGH", date.toString(), true);

        assertThat(results).hasSize(1);
        verify(taskRepository, times(1)).searchTasks(TaskStatus.TODO, TaskPriority.HIGH, date, true);
    }

    @Test
    @DisplayName("Should count tasks using filters")
    void shouldCountTasksUsingFilters() {
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        when(taskRepository.countTasks(null, TaskPriority.LOW, date, false)).thenReturn(5L);

        long count = taskTools.countTasks(null, "LOW", date.toString(), false);

        assertThat(count).isEqualTo(5L);
        verify(taskRepository, times(1)).countTasks(null, TaskPriority.LOW, date, false);
    }
}

