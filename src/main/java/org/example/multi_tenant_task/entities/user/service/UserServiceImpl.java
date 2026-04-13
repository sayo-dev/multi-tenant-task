package org.example.multi_tenant_task.entities.user.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.config.MubaroService;
import org.example.multi_tenant_task.entities.otp.OtpRequest;
import org.example.multi_tenant_task.entities.otp.OtpType;
import org.example.multi_tenant_task.entities.otp.service.OtpService;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.entities.organization.Organization;
import org.example.multi_tenant_task.entities.organization.OrganizationRepository;
import org.example.multi_tenant_task.entities.role.Role;
import org.example.multi_tenant_task.entities.role.RoleEnum;
import org.example.multi_tenant_task.entities.role.RoleRepository;
import org.example.multi_tenant_task.entities.user.CurrentUserUtil;
import org.example.multi_tenant_task.entities.user.JwtService;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.example.multi_tenant_task.entities.user.dto.UserRequest;
import org.example.multi_tenant_task.util.EmailService;
import org.example.multi_tenant_task.util.Helper;
import org.example.multi_tenant_task.util.TokenPair;
import org.springframework.security.access.AccessDeniedException;
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

    private final OrganizationRepository orgRepository;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final CurrentUserUtil currentUserUtil;
    private final MubaroService mubaroService;


    @Transactional
    @Override
    public void createUser(UserRequest request) {
        Organization organization = null;
        if (request.orgId() != null) {

            organization = orgRepository.findById(request.orgId())
                    .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        }

        Optional<User> userCheck = userRepository.findByEmailIgnoreCase(request.email());
        if (userCheck.isPresent()) {
            throw new ConflictException("User already created");
        }

        Role role = roleRepository.findRoleByRole(organization != null ? RoleEnum.MEMBER : RoleEnum.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Role access is invalid"));

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .isVerified(false)
                .password(passwordEncoder.encode(request.password()))
                .organization(organization)
                .role(Set.of(role))
                .build();

        userRepository.save(user);

        String otp = Helper.generateNumericOtp(6);

        //db level interaction
//        otpService.createOtp(OtpRequest.builder()
//                .purpose(OtpType.ACCOUNT_VERIFICATION.name())
//                .email(request.email())
//                .otp(passwordEncoder.encode(otp))
//                .build());

        //redis level interaction
        otpService.generateAndStoreOtp(OtpRequest.builder()
                .otp(otp)
                .purpose(OtpType.ACCOUNT_VERIFICATION.name())
                .email(request.email())
                .build());

        userRepository.save(user);

        try {
            emailService.sendMail(
                    request.email(),
                    OtpType.ACCOUNT_VERIFICATION.name().replaceAll("_", " "),
                    "otp-email",
                    Map.of("otp", otp)
            );
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }

//        external api (mubaro)
//        mubaroService.sendEmail(new OtpRequest(request.email()));

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
