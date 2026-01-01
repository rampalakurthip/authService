package com.emis.auth_service.services;

import com.emis.auth_service.dto.request.ForgotPasswordRequest;
import com.emis.auth_service.dto.request.LoginRequest;
import com.emis.auth_service.dto.response.AuthResponse;
import com.emis.auth_service.dto.response.ForgotPasswordResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
}
