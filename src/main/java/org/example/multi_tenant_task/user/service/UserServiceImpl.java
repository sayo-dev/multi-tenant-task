package org.example.multi_tenant_task.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.role.Role;
import org.example.multi_tenant_task.role.RoleEnum;
import org.example.multi_tenant_task.role.RoleRepository;
import org.example.multi_tenant_task.user.CurrentUserUtil;
import org.example.multi_tenant_task.user.JwtService;
import org.example.multi_tenant_task.user.User;
import org.example.multi_tenant_task.user.UserRepository;
import org.example.multi_tenant_task.user.dto.UserRequest;
import org.example.multi_tenant_task.util.TokenPair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CurrentUserUtil currentUserUtil;


    @Override
    public void createUser(UserRequest request) {

        Optional<User> userCheck = userRepository.findByEmailIgnoreCase(request.email());
        if (userCheck.isPresent()) {
            throw new ConflictException("User already created");
        }

        Role role = roleRepository.findRoleByRole(RoleEnum.MEMBER)
                .orElseThrow(() -> new EntityNotFoundException("Role access is invalid"));

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Set.of(role))
                .build();

        userRepository.save(user);

    }

    @Override
    public TokenPair loginUser(UserRequest request) {

        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                        request.password()));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtService.generateTokenPair(authentication);

    }

    @Transactional
    @Override
    public void addRole(UUID userID, RoleEnum role) {

        User userToUpgrade = userRepository.findUserById(userID).orElseThrow(()
                -> new EntityNotFoundException("User not found"));

        Role upgradeRole = roleRepository.findRoleByRole(role)
                .orElseThrow(() -> new EntityNotFoundException("Role access is invalid"));


        Set<Role> userCurrentRoles = userToUpgrade.getRole();

        if (userCurrentRoles.contains(upgradeRole)) {
            throw new AccessDeniedException("User already has role");
        }
        userCurrentRoles.add(upgradeRole);

        userToUpgrade.setRole(userCurrentRoles);
    }
}
