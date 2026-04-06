package org.example.multi_tenant_task.entities.project.service;

import org.example.multi_tenant_task.entities.project.dto.ProjectResponse;
import org.example.multi_tenant_task.entities.project.dto.ProjectRequest;

import java.util.List;

public interface ProjectService {

    void createProject(ProjectRequest request);

    List<ProjectResponse> getProjects();

    ProjectResponse getProject(Long id);

}
