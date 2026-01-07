package com.emis.auth_service.services.impl;


import com.emis.auth_service.dto.request.ResetPasswordRequest;
import com.emis.auth_service.dto.response.ResetPasswordResponse;
import com.emis.auth_service.exceptions.InvalidTokenException;
import com.emis.auth_service.model.PasswordResetToken;
import com.emis.auth_service.model.UserModel;
import com.emis.auth_service.repository.PasswordResetTokenRepository;
import com.emis.auth_service.repository.UserRepository;
import com.emis.auth_service.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        PasswordResetToken token = tokenRepository.findByToken(request.getResetToken())
                .orElseThrow(() -> new InvalidTokenException("Reset token is invalid"));

        if (token.isExpired() || token.isUsed()) {
            throw new InvalidTokenException("Reset token is invalid or expired");
        }

        UserModel user = token.getUser();

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        token.setUsed(true);
        tokenRepository.save(token);

        log.info("Password reset successfully for user {}", user.getUsername());

        return ResetPasswordResponse.builder()
                .message("Password has been reset successfully")
                .passwordUpdated(true)
                .build();
    }
}
