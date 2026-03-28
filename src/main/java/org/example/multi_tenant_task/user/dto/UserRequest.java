package org.example.multi_tenant_task.user.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.multi_tenant_task.organization.Organization;
import org.example.multi_tenant_task.util.UserView;

public record UserRequest(
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be epmty")
        @JsonView(UserView.Create.class)
        String name,

        @Email(message = "Invalid email")
        @NotNull(message = "Email cannot be null")
        @JsonView(UserView.Login.class)
        String email,

        @NotNull(message = "Password cannot be null")
        @JsonView(UserView.Login.class)
        String password) {
}
