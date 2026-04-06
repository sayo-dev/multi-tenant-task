package org.example.multi_tenant_task.entities.user;

import org.example.multi_tenant_task.entities.organization.Organization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findUserById(UUID id);

    List<User> findUserByOrganization(Organization organization);
}
