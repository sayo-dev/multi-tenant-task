package org.example.multi_tenant_task.project.service;

import org.example.multi_tenant_task.project.dto.ProjectResponse;
import org.example.multi_tenant_task.project.dto.ProjectRequest;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    void createProject(ProjectRequest request);

    List<ProjectResponse> getProjects();

    ProjectResponse getProject(Long id);

}
