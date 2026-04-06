package org.example.multi_tenant_task.entities.otp;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import org.example.multi_tenant_task.util.view.OtpView;

@Builder
public record OtpRequest(

        @JsonView(OtpView.Base.class) String otp,
        @JsonView(OtpView.Base.class) String email,
        @JsonView(OtpView.Verify.class) String purpose) {
}
