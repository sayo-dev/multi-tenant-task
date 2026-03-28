package org.example.multi_tenant_task.user;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.role.RoleEnum;
import org.example.multi_tenant_task.user.dto.UpgradeRoleRequest;
import org.example.multi_tenant_task.user.dto.UserRequest;
import org.example.multi_tenant_task.user.service.UserService;
import org.example.multi_tenant_task.util.ApiResponse;
import org.example.multi_tenant_task.util.TokenPair;
import org.example.multi_tenant_task.util.UserView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/create")

    public ResponseEntity<ApiResponse<String>> create(@JsonView(UserView.Create.class) @Valid @RequestBody UserRequest request) {

        userService.createUser(request);
        return new ResponseEntity<>(ApiResponse.success("User created successfully.", null), HttpStatus.CREATED);
    }

    @PostMapping("/login")

    public ResponseEntity<ApiResponse<TokenPair>> login(

            @JsonView(UserView.Login.class) @RequestBody UserRequest request) {

        TokenPair tokenPair = userService.loginUser(request);
        return new ResponseEntity<>(ApiResponse.success("Login successful.", tokenPair), HttpStatus.OK);
    }

    @PutMapping("/upgrade-role/{userId}")
    public ResponseEntity<ApiResponse<String>> addRole(@PathVariable String userId, @Valid @RequestBody UpgradeRoleRequest request) {
        userService.addRole(UUID.fromString(userId), RoleEnum.valueOf(request.role()));

        return ResponseEntity.ok(ApiResponse.success("Role added successfully", null));
    }
}
