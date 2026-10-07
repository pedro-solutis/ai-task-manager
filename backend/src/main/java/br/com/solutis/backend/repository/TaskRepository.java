package br.com.solutis.backend.repository;

import br.com.solutis.backend.domain.entity.Task;
import br.com.solutis.backend.domain.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @EntityGraph (attributePaths = {"parentTask", "subTasks"})
    Page<Task> findAll(Pageable pageable);

    @EntityGraph (attributePaths = {"parentTask", "subTasks"})
    Optional<Task> findById(UUID id);

    @Query(
        """
            SELECT t FROM Task t
            WHERE (:status IS NULL OR t.status = :status)
            AND (:priority IS NULL OR t.priority = :priority)
            AND (cast(:due_date as timestamp) IS NULL OR t.dueDate <= :due_date)
            AND (:excludeDone = false OR t.status != 'DONE')
        """
    )
    List<Task> searchTasks(
        @Param("status") TaskStatus status,
        @Param("priority") TaskPriority priority,
        @Param("due_date") LocalDateTime dueDate,
        @Param("excludeDone") boolean excludeDone
    );
}
