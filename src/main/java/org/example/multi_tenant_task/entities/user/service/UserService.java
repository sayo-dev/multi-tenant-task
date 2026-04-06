package org.example.multi_tenant_task.entities.user.service;

import org.example.multi_tenant_task.entities.role.RoleEnum;
import org.example.multi_tenant_task.entities.user.dto.UserRequest;
import org.example.multi_tenant_task.util.TokenPair;

import java.util.UUID;

public interface UserService {

    void createUser(UserRequest request);

    TokenPair loginUser(UserRequest request);

    void addRole(UUID userID, RoleEnum role);

}
