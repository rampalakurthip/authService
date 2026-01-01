package com.emis.auth_service.services;

import com.emis.auth_service.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public class TokenProvider {

    public String generateAccessToken(UserModel user) {
        // TODO: Replace with real JWT
        return "access-token-for-" + user.getUsername();
    }

    public String generateRefreshToken(UserModel user) {
        // TODO: Replace with real refresh token logic
        return "refresh-token-for-" + user.getUsername();
    }

    public String validateAndExtractUsernameFromRefreshToken(String refreshToken) {
        // TODO: Implement real validation
        if (refreshToken == null || !refreshToken.startsWith("refresh-token-for-")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        return refreshToken.replace("refresh-token-for-", "");
    }

    public long getAccessTokenExpirySeconds() {
        return 3600L;
    }
}
