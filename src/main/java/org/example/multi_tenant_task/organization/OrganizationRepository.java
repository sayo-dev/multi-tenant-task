package org.example.multi_tenant_task.organization;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
