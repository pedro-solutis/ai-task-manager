package br.com.solutis.backend.domain.entity;

import br.com.solutis.backend.domain.enums.TaskPriority;
import br.com.solutis.backend.domain.enums.TaskStatus;

import br.com.solutis.backend.exception.CircularHierarchyException;
import br.com.solutis.backend.exception.InvalidTaskTransitionException;
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
import java.util.List;
import java.util.ArrayList;

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

    @Column(nullable = false)
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

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> subTasks = new ArrayList<>();

    @Column (name = "deleted", nullable = false)
    private Boolean deleted = false;

    public Task(String title, String description, TaskPriority priority, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.priority = priority != null ? priority : TaskPriority.MEDIUM;
        this.dueDate = dueDate;
    }

    public void updateDetails(String title, String description, TaskPriority priority, LocalDateTime dueDate) {
        if(title != null && !title.isBlank()) this.title = title;
        if(description != null) this.description = description;
        if(priority != null) this.priority = priority;
        if(dueDate != null) this.dueDate = dueDate;
    }

    public void assignParent(Task parentTask) {
        if (this.id != null && parentTask != null) {
            Task current = parentTask;
            while (current != null) {
                if (this.id.equals(current.getId())) {
                    throw new CircularHierarchyException("Circular dependency detected: a task cannot be its own ancestor.");
                }
                current = current.getParentTask();
            }
        }
        
        if (this.parentTask != null && this.parentTask.getSubTasks() != null) {
            this.parentTask.getSubTasks().remove(this);
        }
        
        this.parentTask = parentTask;
        
        if (this.parentTask != null && this.parentTask.getSubTasks() != null) {
            this.parentTask.getSubTasks().add(this);
        }
    }

    public void updateStatus(TaskStatus newStatus) {
        if (newStatus == null) return;

        if (this.status == newStatus) return;

        if (this.status == TaskStatus.DONE) {
            throw new InvalidTaskTransitionException("A completed task (DONE) cannot have its status changed.");
        }

        if (this.status == TaskStatus.TODO && newStatus != TaskStatus.IN_PROGRESS) {
            throw new InvalidTaskTransitionException("A TODO task can only be advanced to IN_PROGRESS.");
        }

        if (this.status == TaskStatus.IN_PROGRESS && newStatus != TaskStatus.DONE && newStatus != TaskStatus.TODO) {
            throw new InvalidTaskTransitionException("An IN_PROGRESS task can only be changed to DONE or TODO.");
        }

        this.status = newStatus;
    }

    public void updateCascadeStatus(TaskStatus newStatus){
        if(newStatus != null){
            this.status = newStatus;
        }
    }

    public void updateDueDate(LocalDateTime dueDate) {
        if (dueDate != null) {
            this.dueDate = dueDate;
        }
    }

}
