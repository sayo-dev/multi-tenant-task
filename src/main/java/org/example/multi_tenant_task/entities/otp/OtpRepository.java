package org.example.multi_tenant_task.entities.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByEmailAndPurposeContainingIgnoreCase(String email, String purpose);
}
