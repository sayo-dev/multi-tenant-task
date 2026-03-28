package org.example.multi_tenant_task.task;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.task.dto.TaskRequest;
import org.example.multi_tenant_task.task.dto.TaskResponse;
import org.example.multi_tenant_task.task.service.TaskService;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/task")
@RestController
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{projectId}/create")
    public ResponseEntity<ApiResponse<String>> createTask(
            @PathVariable Long projectId, @RequestBody TaskRequest request) {

        taskService.createTask(projectId, request);
        return ResponseEntity.ok(ApiResponse.success("Task added successfully", null));
    }

    @GetMapping("/all-tasks")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasks(@RequestParam(required = false) String email) {

        List<TaskResponse> userTasks = taskService.getUserTasks(email);
        return ResponseEntity.ok(ApiResponse.success(userTasks));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PatchMapping("/{taskId}/assign/{email}")
    public ResponseEntity<ApiResponse<String>> assignTask(@PathVariable Long taskId, @PathVariable String email) {

        taskService.assignTask(taskId, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

}
