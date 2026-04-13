package org.example.multi_tenant_task.config;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.otp.OtpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class MubaroService {

    private final RestTemplate restTemplate;

    @Value("${mubaro.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.mubaro.app/api/otp-services";

    public void sendEmail(OtpRequest request) {

        var sendPayload = new MubaroSendRequest(request.email());
        HttpEntity<MubaroSendRequest> entity = new HttpEntity<>(sendPayload, createHeaders());
        restTemplate.exchange(BASE_URL + "/send-otp", HttpMethod.POST, entity, String.class);
    }

    public void verifyOtp(OtpRequest request) {
        var verifyPayload = new MubaroVerifyRequest(request.email(), request.otp());
        HttpEntity<MubaroVerifyRequest> entity = new HttpEntity<>(verifyPayload, createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/verify-otp", HttpMethod.POST, entity, String.class);
        System.out.println(response);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key", apiKey);
        return headers;
    }

    private record MubaroVerifyRequest(String email, String otp) {
    }

    private record MubaroSendRequest(String email) {
    }
}
