package org.example.multi_tenant_task.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link org.example.multi_tenant_task.user.User}
 */
@Value
@Builder
@JsonPropertyOrder({"id", "name", "email"})
public class UserResponse {
    UUID id;
    String name;
    String email;
}