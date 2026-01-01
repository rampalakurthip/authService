package com.emis.auth_service.controller;

import com.emis.auth_service.dto.request.ForgotPasswordRequest;
import com.emis.auth_service.dto.request.LoginRequest;
import com.emis.auth_service.dto.response.AuthResponse;
import com.emis.auth_service.dto.response.ForgotPasswordResponse;

import com.emis.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/auth/refresh
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = extractBearerToken(authHeader);
        AuthResponse response = authService.refresh(token);
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/auth/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    private String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }
}
