package org.example.multi_tenant_task.user.dto;

public record UserRequest(
        String name, String email,
        String password) {
}
