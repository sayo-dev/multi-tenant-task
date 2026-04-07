package org.example.multi_tenant_task.entities.otp.service;

import org.example.multi_tenant_task.entities.otp.OtpRequest;

public interface OtpService {

    void createOtp(OtpRequest request);

    void generateAndStoreOtp(OtpRequest request);

    void resendOtp(OtpRequest request);

    void verifyOtp(OtpRequest request);

}
