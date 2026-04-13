package org.example.multi_tenant_task.config;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.otp.OtpRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduledTasks {

    private final OtpRepository otpRepository;

    @Scheduled(cron = "0 0 */6 * * *")
    public void deleteOtp() {
        otpRepository.deleteAll();

    }
}
