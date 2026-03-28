package org.example.multi_tenant_task.task;

import org.example.multi_tenant_task.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getTasksByUser(User user);

    Optional<Task> getTaskById(Long id);

    Optional<Task> findTaskByIdAndUser(Long id, User user);

    List<Task> findByUserAndDueDateBeforeAndStatusNot(User user, LocalDateTime dueDate, TaskStatus status);
}
