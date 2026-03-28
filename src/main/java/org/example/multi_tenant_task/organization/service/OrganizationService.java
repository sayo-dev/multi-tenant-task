package org.example.multi_tenant_task.organization.service;

import org.example.multi_tenant_task.organization.dto.OrgRequest;
import org.example.multi_tenant_task.user.dto.UserResponse;

import java.util.List;

public interface OrganizationService {

    void createOrg(OrgRequest request);

    List<UserResponse> viewOrgUsers();

}
