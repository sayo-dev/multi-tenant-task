package org.example.multi_tenant_task.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.role.Role;
import org.example.multi_tenant_task.role.RoleEnum;
import org.example.multi_tenant_task.role.RoleRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;


    @PostConstruct
    public void initRoles() {
        for (var roleValue : RoleEnum.values()) {
            roleRepository.findRoleByRole(roleValue).orElseGet(() -> {
                Role role = new Role();
                role.setRole(roleValue);
                return roleRepository.save(role);
            });
        }
    }
}
