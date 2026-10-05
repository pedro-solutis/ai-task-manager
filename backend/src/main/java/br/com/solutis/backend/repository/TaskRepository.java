package br.com.solutis.backend.repository;

import br.com.solutis.backend.domain.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @EntityGraph (attributePaths = {"parentTask", "subTasks"})
    Page<Task> findAll(Pageable pageable);

    @EntityGraph (attributePaths = {"parentTask", "subTasks"})
    Optional<Task> findById(UUID id);

}
