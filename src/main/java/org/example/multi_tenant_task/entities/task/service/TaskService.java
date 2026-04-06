package org.example.multi_tenant_task.entities.task.service;

import org.example.multi_tenant_task.entities.task.TaskStatus;
import org.example.multi_tenant_task.entities.task.dto.TaskRequest;
import org.example.multi_tenant_task.entities.task.dto.TaskResponse;

import java.util.List;

public interface TaskService {

    void createTask(Long projectId, TaskRequest request);

    List<TaskResponse> getUserTasks(String email);

    void assignTask(Long taskId, String email);

    void updateStatus(Long id, TaskStatus status, String userEmail);

    List<TaskResponse> getOverdueTask(String email);
}
