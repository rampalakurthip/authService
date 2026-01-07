// AuthResponse.java (login + refresh)
package com.emis.auth_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;   // e.g. "Bearer"
    private long expiresIn;     // seconds
}
