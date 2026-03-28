package org.example.multi_tenant_task.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.exception.CustomBadRequestException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.project.Project;
import org.example.multi_tenant_task.project.ProjectRepository;
import org.example.multi_tenant_task.role.Role;
import org.example.multi_tenant_task.role.RoleEnum;
import org.example.multi_tenant_task.role.RoleRepository;
import org.example.multi_tenant_task.task.Task;
import org.example.multi_tenant_task.task.TaskRepository;
import org.example.multi_tenant_task.task.TaskStatus;
import org.example.multi_tenant_task.task.dto.TaskRequest;
import org.example.multi_tenant_task.task.dto.TaskResponse;
import org.example.multi_tenant_task.user.CurrentUserUtil;
import org.example.multi_tenant_task.user.User;
import org.example.multi_tenant_task.user.UserRepository;
import org.springframework.stereotype.Service;

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
}
