package org.example.multi_tenant_task.organization;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.organization.dto.OrgRequest;
import org.example.multi_tenant_task.organization.service.OrganizationService;
import org.example.multi_tenant_task.user.dto.UserResponse;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/org")
@RestController
public class OrganizationController {


    private final OrganizationService orgService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createOrg(@RequestBody OrgRequest request) {
        orgService.createOrg(request);
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(ApiResponse.success(null));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {

        return ResponseEntity.ok(ApiResponse.success("Request successful", orgService.viewOrgUsers()));

    }
}
