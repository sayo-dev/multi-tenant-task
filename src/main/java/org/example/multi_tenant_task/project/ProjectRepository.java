package org.example.multi_tenant_task.project;

import org.example.multi_tenant_task.organization.Organization;
import org.example.multi_tenant_task.project.dto.ProjectResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> getAllByOrganization(Organization organization);
}
