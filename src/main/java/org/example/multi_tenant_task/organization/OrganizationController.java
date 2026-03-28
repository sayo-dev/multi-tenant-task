package org.example.multi_tenant_task.organization;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.organization.dto.OrgRequest;
import org.example.multi_tenant_task.organization.service.OrganizationService;
import org.example.multi_tenant_task.user.dto.UserResponse;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/org")
@RestController
public class OrganizationController {


    private final OrganizationService orgService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createOrg(@RequestBody OrgRequest request) {
        orgService.createOrg(request);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {

        return new ResponseEntity<>(ApiResponse.success("Request successful", orgService.viewOrgUsers()), HttpStatus.OK);

    }
}
