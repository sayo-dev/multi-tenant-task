package org.example.multi_tenant_task.entities.project.service;

import org.example.multi_tenant_task.entities.project.dto.ProjectResponse;
import org.example.multi_tenant_task.entities.project.dto.ProjectRequest;
import org.example.multi_tenant_task.entities.user.User;

import java.util.List;

public interface ProjectService {

    void createProject(ProjectRequest request, User user);

    List<ProjectResponse> getProjects(User user);

    ProjectResponse getProject(Long id, User user);

    void deleteProject(Long id, User user);

}
