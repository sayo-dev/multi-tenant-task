package org.example.multi_tenant_task.project;

import org.example.multi_tenant_task.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> getAllByOrganization(Organization organization);

    Optional<Project> getProjectById(Long id);
}
