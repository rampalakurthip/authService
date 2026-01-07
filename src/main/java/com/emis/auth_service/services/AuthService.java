package com.emis.auth_service.services;

import com.emis.auth_service.dto.request.ForgotPasswordRequest;
import com.emis.auth_service.dto.request.LoginRequest;
import com.emis.auth_service.dto.response.TokenResponse;
import com.emis.auth_service.dto.response.ForgotPasswordResponse;

public interface AuthService {

    TokenResponse login(LoginRequest request);

    TokenResponse refresh(String authHeader);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
}
