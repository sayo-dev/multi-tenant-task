package org.example.multi_tenant_task.user.service;

import org.example.multi_tenant_task.role.RoleEnum;
import org.example.multi_tenant_task.user.dto.UserRequest;
import org.example.multi_tenant_task.util.TokenPair;

import java.util.UUID;

public interface UserService {

    void createUser(Long orgId, UserRequest request);

    TokenPair loginUser(UserRequest request);

    void addRole(UUID userID, RoleEnum role);

}
