package org.example.multi_tenant_task.util;

import lombok.Data;

@Data
public class TokenPair {

    private final String accessToken;
    private final String refreshToken;
}
