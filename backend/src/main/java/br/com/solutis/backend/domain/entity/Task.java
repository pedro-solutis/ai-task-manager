package br.com.solutis.backend.domain.entity;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@SQLRestriction ("deleted = false")
@SQLDelete(sql = "UPDATE tasks SET deleted = true WHERE id = ?")
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @Column (name = "deleted", nullable = false)
    private Boolean deleted = false;

    public Task(String title, String description, String status, String priority, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status != null ? TaskStatus.valueOf(status.toUpperCase()) : TaskStatus.TODO;
        this.priority = priority != null ? TaskPriority.valueOf(priority.toUpperCase()) : TaskPriority.MEDIUM;
        this.dueDate = dueDate;
    }

    public void updateDetails(String title, String description, String priority, LocalDateTime dueDate) {
        if(title != null) this.title = title;
        if(description != null) this.description = description;
        if(priority != null) this.priority = TaskPriority.valueOf(priority.toUpperCase());
        if(dueDate != null) this.dueDate = dueDate;
    }

    public void assignParent(Task parentTask) {
        if (parentTask != null && parentTask.getId().equals(this.id)) {
            throw new IllegalArgumentException("A task cannot be its own parent.");
        }
        this.parentTask = parentTask;
    }

    public void updateStatus(String newStatusStr) {
        if (newStatusStr == null) return;
        
        TaskStatus newStatus = TaskStatus.valueOf(newStatusStr.toUpperCase());

        if (this.status == newStatus) return;

        if (this.status == TaskStatus.DONE) {
            throw new IllegalStateException("A completed task (DONE) cannot have its status changed.");
        }

        if (this.status == TaskStatus.TODO && newStatus != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("A TODO task can only be advanced to IN_PROGRESS.");
        }

        if (this.status == TaskStatus.IN_PROGRESS && newStatus != TaskStatus.DONE) {
            throw new IllegalStateException("A IN_PROGRESS task can only be completed to DONE.");
        }

        this.status = newStatus;
    }

}
