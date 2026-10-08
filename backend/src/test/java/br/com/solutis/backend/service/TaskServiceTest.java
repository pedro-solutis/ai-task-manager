package br.com.solutis.backend.service;

import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.dto.request.ParentTaskUpdateDTO;
import br.com.solutis.backend.dto.request.TaskCreationDTO;
import br.com.solutis.backend.dto.request.TaskStatusUpdateDTO;
import br.com.solutis.backend.dto.request.TaskUpdateDTO;
import br.com.solutis.backend.dto.response.TaskDecomposeResponseDTO;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = new Task("Test Title", "Test Desc", TaskPriority.MEDIUM, LocalDateTime.now().plusDays(1));
        try {
            var idField = Task.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(task, taskId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should find all tasks")
    void shouldFindAll() {
        List<Task> list = List.of(task);
        when(taskRepository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(list);

        List<TaskResponseDTO> result = taskService.findAll("dueDate", "DESC");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().title()).isEqualTo("Test Title");
        verify(taskRepository, times(1)).findAll(any(org.springframework.data.domain.Sort.class));
    }

    @Test
    @DisplayName("Should find task by id successfully")
    void shouldFindById() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskResponseDTO result = taskService.findById(taskId);

        assertThat(result.title()).isEqualTo("Test Title");
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when task not found by id")
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    @DisplayName("Should create task successfully without parent")
    void shouldCreateTask() {
        TaskCreationDTO dto = new TaskCreationDTO("New Task", "Desc", "HIGH", LocalDateTime.now().plusDays(2), null);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            return saved;
        });

        TaskResponseDTO result = taskService.create(dto);

        assertThat(result.title()).isEqualTo("New Task");
        assertThat(result.priority()).isEqualTo(TaskPriority.HIGH);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should create task with parent successfully")
    void shouldCreateTaskWithParent() {
        UUID parentId = UUID.randomUUID();
        Task parent = new Task("Parent", "Desc", TaskPriority.MEDIUM, LocalDateTime.now());
        
        TaskCreationDTO dto = new TaskCreationDTO("Child", "Desc", "LOW", LocalDateTime.now(), parentId);
        when(taskRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        TaskResponseDTO result = taskService.create(dto);

        assertThat(result.title()).isEqualTo("Child");
        verify(taskRepository, times(1)).findById(parentId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task details successfully")
    void shouldUpdateTask() {
        TaskUpdateDTO dto = new TaskUpdateDTO("Updated Title", "Updated Desc", "LOW", LocalDateTime.now().plusDays(5));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.update(taskId, dto);

        assertThat(result.title()).isEqualTo("Updated Title");
        assertThat(task.getPriority()).isEqualTo(TaskPriority.LOW);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTask() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.delete(taskId);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Should update task status successfully")
    void shouldUpdateStatus() {
        TaskStatusUpdateDTO dto = new TaskStatusUpdateDTO("IN_PROGRESS");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.updateStatus(taskId, dto);

        assertThat(result.status()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update parent successfully")
    void shouldUpdateParent() {
        UUID newParentId = UUID.randomUUID();
        Task newParent = new Task("New Parent", "Desc", TaskPriority.HIGH, LocalDateTime.now());
        ParentTaskUpdateDTO dto = new ParentTaskUpdateDTO(newParentId.toString());
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.findById(newParentId)).thenReturn(Optional.of(newParent));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.updateParent(taskId, dto);

        assertThat(task.getParentTask()).isEqualTo(newParent);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should save decomposed tasks correctly")
    void shouldSaveDecomposedTasks() {
        var subTaskDto = new br.com.solutis.backend.dto.response.SubTaskDTO("Sub1", "Desc", TaskPriority.LOW, null);
        TaskDecomposeResponseDTO dto = new TaskDecomposeResponseDTO(List.of(subTaskDto));
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        List<TaskResponseDTO> results = taskService.saveDecomposedTasks(taskId, dto);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().title()).isEqualTo("Sub1");
        verify(taskRepository, times(1)).saveAll(any());
    }
}

