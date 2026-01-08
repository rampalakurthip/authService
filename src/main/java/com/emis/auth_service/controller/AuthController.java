package com.emis.auth_service.controller;

import com.emis.auth_service.dto.request.ForgotPasswordRequest;
import com.emis.auth_service.dto.request.LoginRequest;
import com.emis.auth_service.dto.request.ResetPasswordRequest;
import com.emis.auth_service.dto.response.AuthBaseResponse;
import com.emis.auth_service.dto.response.ResetPasswordResponse;
import com.emis.auth_service.dto.response.TokenResponse;
import com.emis.auth_service.dto.response.ForgotPasswordResponse;

import com.emis.auth_service.services.AuthService;
import com.emis.auth_service.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    // POST /api/v1/auth/login
    @PostMapping("/login")
    public AuthBaseResponse<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return AuthBaseResponse.<TokenResponse>builder()
                .data(response)
                .message("Login successful")
                .status(200)
                .build();
    }

    // POST /api/v1/auth/refresh
    @PostMapping("/refresh")
    public AuthBaseResponse<TokenResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        TokenResponse response = authService.refresh(authHeader);
        return AuthBaseResponse.<TokenResponse>builder()
                .data(response)
                .message("Token refreshed successfully")
                .status(200)
                .build();
    }

    // POST /api/v1/auth/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        ResetPasswordResponse response = passwordResetService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

}
