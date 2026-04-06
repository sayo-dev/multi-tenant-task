package org.example.multi_tenant_task.entities.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.exception.CustomBadRequestException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.entities.project.Project;
import org.example.multi_tenant_task.entities.project.ProjectRepository;
import org.example.multi_tenant_task.entities.role.Role;
import org.example.multi_tenant_task.entities.role.RoleEnum;
import org.example.multi_tenant_task.entities.role.RoleRepository;
import org.example.multi_tenant_task.entities.task.Task;
import org.example.multi_tenant_task.entities.task.TaskRepository;
import org.example.multi_tenant_task.entities.task.TaskStatus;
import org.example.multi_tenant_task.entities.task.dto.TaskRequest;
import org.example.multi_tenant_task.entities.task.dto.TaskResponse;
import org.example.multi_tenant_task.entities.user.CurrentUserUtil;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserUtil currentUserUtil;

    @Override
    public void createTask(Long projectId, TaskRequest request) {

        Project project = projectRepository.getProjectById(projectId).orElseThrow(()
                -> new EntityNotFoundException("Project not found"));

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO)
                .project(project)
                .dueDate(request.dueDate())
                .build();

        taskRepository.save(task);

    }

    @Override
    public List<TaskResponse> getUserTasks(String email) {

        User user = getUser(email);

        return taskRepository.getTasksByUser(user).stream().map(
                task -> TaskResponse.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .status(task.getStatus())
                        .dueDate(task.getDueDate())
                        .build()).toList();
    }


    @Transactional
    @Override
    public void assignTask(Long taskId, String email) {

        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new EntityNotFoundException("User not found"));

        Task task = taskRepository.getTaskById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (user.getTasks().contains(task)) throw new ConflictException("Task already assigned to user");

        List<Task> tasks = user.getTasks();
        tasks.add(task);
        task.setUser(user);
    }

    @Transactional
    @Override
    public void updateStatus(Long id, TaskStatus status, String userEmail) {

        User user = getUser(userEmail);

        Task task = taskRepository.findTaskByIdAndUser(id, user).orElseThrow(()
                -> new EntityNotFoundException("Task not found"));

        if (task.getStatus() == TaskStatus.TODO && status == TaskStatus.DONE)
            throw new CustomBadRequestException("Only task marked as progress can be done");

        if (task.getStatus() == TaskStatus.DONE)
            throw new CustomBadRequestException("Task is already completed");

        if (task.getStatus() == status)
            throw new CustomBadRequestException("Task status not changed");

        task.setStatus(status);
    }


    @Override
    public List<TaskResponse> getOverdueTask(String email) {
        User user = getUser(email);

        List<Task> tasks = taskRepository.findByUserAndDueDateBeforeAndStatusNot(user, LocalDateTime.now(), TaskStatus.DONE);
        return tasks.stream().map(task -> TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build()).toList();
    }

    private @NonNull User getUser(String email) {
        User user;
        User loggedInUser = currentUserUtil.getLoggedInUser();

        Role admin = roleRepository.findRoleByRole(RoleEnum.ADMIN).orElseThrow(() -> new CustomBadRequestException("Role mismatch"));
        Role manager = roleRepository.findRoleByRole(RoleEnum.MANAGER).orElseThrow(() -> new CustomBadRequestException("Role mismatch"));

        boolean hasPermission = loggedInUser.getRole().stream().anyMatch(role -> role == admin || role == manager);
        if (hasPermission) {
            user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                    new EntityNotFoundException("User not found"));
        } else {
            user = loggedInUser;
        }
        return user;
    }

}
