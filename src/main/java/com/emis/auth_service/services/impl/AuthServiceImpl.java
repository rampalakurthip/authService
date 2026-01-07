package com.emis.auth_service.services.impl;

import com.emis.auth_service.dto.request.ForgotPasswordRequest;
import com.emis.auth_service.dto.request.LoginRequest;
import com.emis.auth_service.dto.response.TokenResponse;
import com.emis.auth_service.dto.response.ForgotPasswordResponse;
import com.emis.auth_service.exceptions.InvalidCredentialsException;
import com.emis.auth_service.exceptions.InvalidTokenException;
import com.emis.auth_service.model.UserModel;
import com.emis.auth_service.provider.keycloak.KeycloakAuthProvider;
import com.emis.auth_service.provider.keycloak.client.response.KeycloakTokenResponse;
import com.emis.auth_service.repository.UserRepository;
import com.emis.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakAuthProvider keycloakAuthProvider;

    @Override
    public TokenResponse login(LoginRequest request) {
        UserModel user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        KeycloakTokenResponse keycloakTokenResponse = keycloakAuthProvider.generateToken(user.getUsername());
        return TokenResponse.builder()
                .accessToken(keycloakTokenResponse.accessToken())
                .refreshToken(keycloakTokenResponse.refreshToken())
                .tokenType("Bearer")
                .expiresIn(keycloakTokenResponse.expiresIn())
                .build();
    }

    @Override
    public TokenResponse refresh(String authHeader) {
        if (!StringUtils.isNotEmpty(authHeader)) {
            throw new InvalidTokenException("Invalid Authorization header");
        }

        String refreshToken = authHeader.replace("Bearer ", "").trim();

        try {
            // Pure Keycloak refresh - NO user lookup!
            KeycloakTokenResponse keycloakTokens = keycloakAuthProvider.refreshToken(refreshToken);

            return TokenResponse.builder()
                    .accessToken(keycloakTokens.accessToken())
                    .refreshToken(keycloakTokens.refreshToken())
                    .tokenType(keycloakTokens.tokenType())
                    .expiresIn(keycloakTokens.expiresIn())
                    .build();

        } catch (ResponseStatusException ex) {
            log.error("Keycloak refresh failed: {}", ex.getReason());
            throw new InvalidTokenException("Refresh token invalid or expired");
        } catch (Exception ex) {
            log.error("Refresh failed", ex);
            throw new InvalidTokenException("Token refresh failed");
        }
    }


    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        return userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                .map(user -> {
                    // TODO: generate token, send email
                    log.info("Sending password reset email to {}", user.getLoginEmail());

                    return ForgotPasswordResponse.builder()
                            .message("Password reset link sent to " + user.getLoginEmail())
                            .emailSent(true)
                            .build();
                })
                .orElseGet(() -> ForgotPasswordResponse.builder()
                        .message("If the account exists, a reset link has been sent")
                        .emailSent(false)
                        .build());
    }

}
