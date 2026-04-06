package org.example.multi_tenant_task.entities.organization.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.entities.organization.Organization;
import org.example.multi_tenant_task.entities.organization.OrganizationRepository;
import org.example.multi_tenant_task.entities.organization.dto.OrgRequest;
import org.example.multi_tenant_task.entities.role.RoleRepository;
import org.example.multi_tenant_task.entities.user.CurrentUserUtil;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.example.multi_tenant_task.entities.user.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository orgRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserUtil currentUserUtil;

    @Transactional
    @Override
    public void createOrg(OrgRequest request) {

        User user = currentUserUtil.getLoggedInUser();

        if (user.getOrganization() != null)
            throw new ConflictException("User already belong to an organization");

        Organization org = Organization.builder()
                .name(request.name())
                .createdAt(LocalDateTime.now())
                .build();

        user.setOrganization(org);
        org.setUser(List.of(user));
        orgRepository.save(org);
    }

    @Override
    public List<UserResponse> viewOrgUsers() {

        User user = currentUserUtil.getLoggedInUser();

        return userRepository.findUserByOrganization(user.getOrganization())
                .stream().map(u -> UserResponse.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .build()).toList();

    }
}
