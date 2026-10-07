package br.com.solutis.backend.repository;

import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task("Task 1", "Desc 1", TaskPriority.HIGH, LocalDateTime.now().plusDays(2));
        task2 = new Task("Task 2", "Desc 2", TaskPriority.LOW, LocalDateTime.now().plusDays(5));
        
        task1 = entityManager.persistAndFlush(task1);
        task2 = entityManager.persistAndFlush(task2);
    }

    @Test
    @DisplayName("Should find all tasks not deleted")
    void shouldFindAllTasks() {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(0, 10));
        
        assertThat(tasks.getContent()).hasSize(2);
        assertThat(tasks.getContent()).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    @DisplayName("Should logically delete a task and not find it afterwards")
    void shouldDeleteLogically() {
        taskRepository.delete(task1);
        entityManager.flush();
        entityManager.clear();

        Optional<Task> found = taskRepository.findById(task1.getId());
        assertThat(found).isEmpty();
        
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(0, 10));
        assertThat(tasks.getContent()).hasSize(1);
        assertThat(tasks.getContent().getFirst().getTitle()).isEqualTo("Task 2");
    }

    @Test
    @DisplayName("Should search tasks with filters correctly")
    void shouldSearchTasksWithFilters() {
        task1.updateStatus(TaskStatus.IN_PROGRESS);
        entityManager.persistAndFlush(task1);

        List<Task> result1 = taskRepository.searchTasks(TaskStatus.IN_PROGRESS, null, null, false);
        assertThat(result1).hasSize(1).contains(task1);

        List<Task> result2 = taskRepository.searchTasks(null, TaskPriority.LOW, null, false);
        assertThat(result2).hasSize(1).contains(task2);
        
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        List<Task> result3 = taskRepository.searchTasks(null, null, pastDate, false);
        assertThat(result3).isEmpty();
    }

    @Test
    @DisplayName("Should count tasks with filters correctly")
    void shouldCountTasksWithFilters() {
        task2.updateStatus(TaskStatus.IN_PROGRESS);
        task2.updateStatus(TaskStatus.DONE);
        entityManager.persistAndFlush(task2);

        long countAll = taskRepository.countTasks(null, null, null, false);
        assertThat(countAll).isEqualTo(2);

        long countExcludeDone = taskRepository.countTasks(null, null, null, true);
        assertThat(countExcludeDone).isEqualTo(1);
    }
}

