package org.example.multi_tenant_task.entities.otp.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.otp.Otp;
import org.example.multi_tenant_task.entities.otp.OtpRepository;
import org.example.multi_tenant_task.entities.otp.OtpRequest;
import org.example.multi_tenant_task.entities.otp.OtpType;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.example.multi_tenant_task.exception.ConflictException;
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

    @Transactional
    @Override
    public void verifyOtp(OtpRequest request) {

        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String key = getKey(request.email());

        String hashedOtp = (String) redisTemplate.opsForValue().get(key);

        System.out.println("========HASHED FROM REDIS=======");
        System.out.println(hashedOtp);
        System.out.println("========PLAIN OTP=======");
        System.out.println(request.otp());

        if (hashedOtp == null) throw new ConflictException("Otp expired");

        System.out.println("=======Password match check======");
        System.out.println(!passwordEncoder.matches(request.otp(), hashedOtp));
        if (!passwordEncoder.matches(request.otp(), hashedOtp))
            throw new CustomBadRequestException("Invalid Otp");

        redisTemplate.delete(key);


        user.setIsVerified(true);
        userRepository.save(user);

//        User user = userRepository.findByEmailIgnoreCase(request.email())
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        if (user.getIsVerified()) throw new CustomBadRequestException("User already verified");
//
//        Otp otp = otpRepository.findByEmailAndPurposeContainingIgnoreCase(request.email(), request.purpose())
//                .orElseThrow(() -> new EntityNotFoundException("Otp not found"));
//
//        if (!passwordEncoder.matches(request.otp(), otp.getOtp())) throw new ConflictException("Invalid otp");
//
//        user.setIsVerified(true);

    }

    private String getKey(String email) {
        return "otp:".concat(email);
    }
}
