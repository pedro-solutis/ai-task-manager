package br.com.solutis.backend.domain.entity;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import br.com.solutis.backend.exception.CircularHierarchyException;
import br.com.solutis.backend.exception.InvalidTaskTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Test Task", "Test Description", TaskPriority.MEDIUM, LocalDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("Should create task with correct initial values")
    void shouldCreateTaskWithCorrectInitialValues() {
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Test Description");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(task.getDeleted()).isFalse();
    }

    @Test
    @DisplayName("Should update task details successfully")
    void shouldUpdateTaskDetailsSuccessfully() {
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(5);
        task.updateDetails("Updated Title", "Updated Description", TaskPriority.HIGH, newDueDate);

        assertThat(task.getTitle()).isEqualTo("Updated Title");
        assertThat(task.getDescription()).isEqualTo("Updated Description");
        assertThat(task.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(task.getDueDate()).isEqualTo(newDueDate);
    }

    @Test
    @DisplayName("Should update task status successfully for valid transitions")
    void shouldUpdateTaskStatusSuccessfully() {
        task.updateStatus(TaskStatus.IN_PROGRESS);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        task.updateStatus(TaskStatus.TODO);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);

        task.updateStatus(TaskStatus.IN_PROGRESS);
        task.updateStatus(TaskStatus.DONE);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    @DisplayName("Should throw exception when updating status with invalid transitions")
    void shouldThrowExceptionOnInvalidStatusTransitions() {
        assertThatThrownBy(() -> task.updateStatus(TaskStatus.DONE))
                .isInstanceOf(InvalidTaskTransitionException.class)
                .hasMessageContaining("A TODO task can only be advanced to IN_PROGRESS");

        task.updateStatus(TaskStatus.IN_PROGRESS);
        task.updateStatus(TaskStatus.DONE);

        assertThatThrownBy(() -> task.updateStatus(TaskStatus.IN_PROGRESS))
                .isInstanceOf(InvalidTaskTransitionException.class)
                .hasMessageContaining("A completed task (DONE) cannot have its status changed");
    }

    @Test
    @DisplayName("Should assign parent task successfully")
    void shouldAssignParentTaskSuccessfully() {
        Task parent = new Task("Parent", "Desc", TaskPriority.HIGH, LocalDateTime.now());
        
        task.assignParent(parent);

        assertThat(task.getParentTask()).isEqualTo(parent);
        assertThat(parent.getSubTasks()).contains(task);
    }

    @Test
    @DisplayName("Should remove from old parent when assigning new parent")
    void shouldRemoveFromOldParentWhenAssigningNew() {
        Task oldParent = new Task("Old Parent", "Desc", TaskPriority.HIGH, LocalDateTime.now());
        Task newParent = new Task("New Parent", "Desc", TaskPriority.HIGH, LocalDateTime.now());
        
        task.assignParent(oldParent);
        assertThat(oldParent.getSubTasks()).contains(task);

        task.assignParent(newParent);
        assertThat(oldParent.getSubTasks()).doesNotContain(task);
        assertThat(newParent.getSubTasks()).contains(task);
        assertThat(task.getParentTask()).isEqualTo(newParent);
    }

    @Test
    @DisplayName("Should throw exception on circular dependency")
    void shouldThrowExceptionOnCircularDependency() {
        Task task1 = new Task(UUID.randomUUID(), "Task 1", "Desc", TaskStatus.TODO, TaskPriority.MEDIUM, LocalDateTime.now(), null, null, null, new ArrayList<>(), false);
        Task task2 = new Task(UUID.randomUUID(), "Task 2", "Desc", TaskStatus.TODO, TaskPriority.MEDIUM, LocalDateTime.now(), null, null, null, new ArrayList<>(), false);
        Task task3 = new Task(UUID.randomUUID(), "Task 3", "Desc", TaskStatus.TODO, TaskPriority.MEDIUM, LocalDateTime.now(), null, null, null, new ArrayList<>(), false);
        
        task2.assignParent(task1);
        task3.assignParent(task2);

        assertThatThrownBy(() -> task1.assignParent(task3))
                .isInstanceOf(CircularHierarchyException.class)
                .hasMessageContaining("Circular dependency detected");
    }
}

