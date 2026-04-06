package org.example.multi_tenant_task.entities.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpgradeRoleRequest(
        @NotNull @NotBlank String role
) {
}
