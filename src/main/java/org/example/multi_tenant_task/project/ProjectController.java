package org.example.multi_tenant_task.project;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.project.dto.ProjectRequest;
import org.example.multi_tenant_task.project.dto.ProjectResponse;
import org.example.multi_tenant_task.project.service.ProjectService;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createTask(@RequestBody ProjectRequest request) {

        projectService.createProject(request);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjects() {

        List<ProjectResponse> projects = projectService.getProjects();
        return new ResponseEntity<>(ApiResponse.success(projects), HttpStatus.OK);

    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {

        ProjectResponse project = projectService.getProject(id);
        return new ResponseEntity<>(ApiResponse.success(project), HttpStatus.OK);
    }
}
