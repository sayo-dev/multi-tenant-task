package org.example.multi_tenant_task.entities.otp.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.config.MubaroService;
import org.example.multi_tenant_task.entities.otp.Otp;
import org.example.multi_tenant_task.entities.otp.OtpRepository;
import org.example.multi_tenant_task.entities.otp.OtpRequest;
import org.example.multi_tenant_task.entities.otp.OtpType;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.example.multi_tenant_task.exception.CustomBadRequestException;
import org.example.multi_tenant_task.exception.EntityNotFoundException;
import org.example.multi_tenant_task.util.EmailService;
import org.example.multi_tenant_task.util.Helper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MubaroService mubaroService;

    private static final long OTP_EXPIRY_MINUTES = 5;

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

        try {
            //use mubaro (third party for otp flow)
            mubaroService.verifyOtp(request);
        } catch (Exception e) {
            // fallback to Redis if Mubaro fails or is not used for this request
            String key = getKey(request.email());
            String hashedOtp = (String) redisTemplate.opsForValue().get(key);

            if (hashedOtp == null || !passwordEncoder.matches(request.otp(), hashedOtp)) {
                throw new CustomBadRequestException("Invalid or expired OTP");
            }
            redisTemplate.delete(key);
        }

        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public void generateAndStoreOtp(OtpRequest request) {

        String key = getKey(request.email());

        String hashedOtp = passwordEncoder.encode(request.otp());

        System.out.println("========HASHED OTP========");
        System.out.println(hashedOtp);

        redisTemplate.opsForValue().set(
                key,
                hashedOtp,
                Duration.ofMinutes(OTP_EXPIRY_MINUTES)
        );

    }

    @Transactional
    @Override
    public void resendOtp(OtpRequest request) {
        userRepository.findByEmailIgnoreCase(request.email()).orElseThrow(()
                -> new EntityNotFoundException("User not found"));

        String otp = Helper.generateNumericOtp(6);
        generateAndStoreOtp(OtpRequest.builder()
                .otp(otp)
                .email(request.email())
                .purpose(OtpType.ACCOUNT_VERIFICATION.name())
                .build());

        try {
            emailService.sendMail(request.email(),

                    OtpType.ACCOUNT_VERIFICATION.name().replaceAll("_", " "),
                    "otp-email",
                    Map.of("otp", otp));
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getKey(String email) {
        return "otp:".concat(email);
    }
}
