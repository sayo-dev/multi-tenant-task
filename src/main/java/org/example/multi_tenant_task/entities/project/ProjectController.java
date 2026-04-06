package org.example.multi_tenant_task.entities.project;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.project.dto.ProjectRequest;
import org.example.multi_tenant_task.entities.project.dto.ProjectResponse;
import org.example.multi_tenant_task.entities.project.service.ProjectService;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createProject(@RequestBody ProjectRequest request) {

        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", null));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjects() {

        List<ProjectResponse> projects = projectService.getProjects();
        return ResponseEntity.ok(ApiResponse.success(projects));

    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {

        ProjectResponse project = projectService.getProject(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
}
