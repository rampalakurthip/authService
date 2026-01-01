package com.emis.auth_service.services.impl;

import com.emis.auth_service.dto.request.ForgotPasswordRequest;
import com.emis.auth_service.dto.request.LoginRequest;
import com.emis.auth_service.dto.response.AuthResponse;
import com.emis.auth_service.dto.response.ForgotPasswordResponse;
import com.emis.auth_service.exceptions.InvalidCredentialsException;
import com.emis.auth_service.exceptions.InvalidTokenException;
import com.emis.auth_service.model.UserModel;
import com.emis.auth_service.repository.UserRepository;
import com.emis.auth_service.services.TokenProvider;
import com.emis.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public AuthResponse login(LoginRequest request) {
        UserModel user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getAccessTokenExpirySeconds())
                .build();
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        try {
            String username = tokenProvider.validateAndExtractUsernameFromRefreshToken(refreshToken);

            UserModel user = userRepository.findByUsernameIgnoreCase(username)
                    .orElseThrow(() -> new InvalidTokenException("User not found for refresh token"));

            String newAccessToken = tokenProvider.generateAccessToken(user);
            String newRefreshToken = tokenProvider.generateRefreshToken(user); // or reuse old

            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(tokenProvider.getAccessTokenExpirySeconds())
                    .build();

        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid refresh token");
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
