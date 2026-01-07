package com.emis.auth_service.services;


import com.emis.auth_service.dto.request.ResetPasswordRequest;
import com.emis.auth_service.dto.response.ResetPasswordResponse;

public interface PasswordResetService {

    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
}
