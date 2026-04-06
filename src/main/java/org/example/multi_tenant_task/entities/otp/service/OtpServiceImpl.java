package org.example.multi_tenant_task.entities.otp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.otp.Otp;
import org.example.multi_tenant_task.entities.otp.OtpRepository;
import org.example.multi_tenant_task.entities.otp.OtpRequest;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.example.multi_tenant_task.exception.ConflictException;
import org.example.multi_tenant_task.exception.CustomBadRequestException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.util.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createOtp(OtpRequest request) {

        Otp otp = Otp.builder()
                .email(request.email())
                .otp(request.otp())
                .purpose(request.purpose())
                .build();

        otpRepository.save(otp);
    }

    @Transactional
    @Override
    public void verifyOtp(OtpRequest request) {

        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getIsVerified()) throw new CustomBadRequestException("User already verified");

        Otp otp = otpRepository.findByEmailAndPurposeContainingIgnoreCase(request.email(), request.purpose())
                .orElseThrow(() -> new EntityNotFoundException("Otp not found"));

        if (!passwordEncoder.matches(request.otp(), otp.getOtp())) throw new ConflictException("Invalid otp");

        user.setIsVerified(true);
    }
}
