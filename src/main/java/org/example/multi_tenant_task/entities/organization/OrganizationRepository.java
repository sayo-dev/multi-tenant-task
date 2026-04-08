package org.example.multi_tenant_task.entities.organization;

import org.example.multi_tenant_task.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByUserId(UUID id);
}
