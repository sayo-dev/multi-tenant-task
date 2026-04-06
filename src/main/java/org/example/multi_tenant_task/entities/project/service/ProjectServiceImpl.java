package org.example.multi_tenant_task.entities.project.service;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.entities.project.Project;
import org.example.multi_tenant_task.entities.project.ProjectRepository;
import org.example.multi_tenant_task.entities.project.dto.ProjectRequest;
import org.example.multi_tenant_task.entities.project.dto.ProjectResponse;
import org.example.multi_tenant_task.entities.role.Role;
import org.example.multi_tenant_task.entities.role.RoleEnum;
import org.example.multi_tenant_task.entities.role.RoleRepository;
import org.example.multi_tenant_task.entities.user.CurrentUserUtil;
import org.example.multi_tenant_task.entities.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserUtil currentUserUtil;


    @Override
    public void createProject(ProjectRequest request) {

        User user = currentUserUtil.getLoggedInUser();

        Role adminRole = roleRepository.findRoleByRole(RoleEnum.ADMIN).orElseThrow(() -> new EntityNotFoundException("Role mismatch"));
        Role managerRole = roleRepository.findRoleByRole(RoleEnum.MANAGER).orElseThrow(() -> new EntityNotFoundException("Role mismatch"));


        if (!(user.getRole().contains(adminRole) || user.getRole().contains(managerRole)))
            throw new AccessDeniedException("You don't have permission for this operation");
        Project project = Project.builder()
                .title(request.title())
                .description(request.description())
                .user(user)
                .build();

        project.setOrganization(user.getOrganization());

        projectRepository.save(project);

    }

    @Override
    public List<ProjectResponse> getProjects() {

        User user = currentUserUtil.getLoggedInUser();

        return projectRepository.getAllByOrganization(user.getOrganization()).stream()
                .map((project -> ProjectResponse.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .description(project.getDescription())
                        .build())).toList();
    }

    @Override
    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.getProjectById(id).orElseThrow(()
                -> new EntityNotFoundException("Project not found"));
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .build();
    }
}
