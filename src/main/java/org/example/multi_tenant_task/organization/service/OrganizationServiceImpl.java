package org.example.multi_tenant_task.organization.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.organization.Organization;
import org.example.multi_tenant_task.organization.OrganizationRepository;
import org.example.multi_tenant_task.organization.dto.OrgRequest;
import org.example.multi_tenant_task.role.Role;
import org.example.multi_tenant_task.role.RoleEnum;
import org.example.multi_tenant_task.role.RoleRepository;
import org.example.multi_tenant_task.user.CurrentUserUtil;
import org.example.multi_tenant_task.user.User;
import org.example.multi_tenant_task.user.UserRepository;
import org.example.multi_tenant_task.user.dto.UserResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
