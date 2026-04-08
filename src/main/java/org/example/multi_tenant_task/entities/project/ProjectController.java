package org.example.multi_tenant_task.entities.project;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.project.dto.ProjectRequest;
import org.example.multi_tenant_task.entities.project.dto.ProjectResponse;
import org.example.multi_tenant_task.entities.project.service.ProjectService;
import org.example.multi_tenant_task.entities.user.CurrentUserUtil;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/v1/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final CurrentUserUtil currentUserUtil;


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createProject(@RequestBody ProjectRequest request) {

        var user = currentUserUtil.getLoggedInUser();
        projectService.createProject(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", null));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjects() {

        var user = currentUserUtil.getLoggedInUser();
        List<ProjectResponse> projects = projectService.getProjects(user);
        return ResponseEntity.ok(ApiResponse.success(projects));

    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        var user = currentUserUtil.getLoggedInUser();
        ProjectResponse project = projectService.getProject(id, user);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(@PathVariable Long id) {

        var user = currentUserUtil.getLoggedInUser();
        projectService.deleteProject(id, user);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", null));

    }
}
