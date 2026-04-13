package org.example.multi_tenant_task.entities.otp;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import org.example.multi_tenant_task.util.view.OtpView;

@Builder
public record OtpRequest(

        @JsonView(OtpView.Base.class) String otp,
        @JsonView({OtpView.Base.class, OtpView.Resend.class}) String email,
        @JsonView(OtpView.Purpose.class) String purpose) {

    public OtpRequest(String email) {
        this(null, email, null);
    }
}
