package org.example.multi_tenant_task.entities.organization.service;

import org.example.multi_tenant_task.entities.organization.dto.OrgRequest;
import org.example.multi_tenant_task.entities.user.dto.UserResponse;

import java.util.List;

public interface OrganizationService {

    void createOrg(OrgRequest request);

    List<UserResponse> viewOrgUsers();

}
