package com.emis.auth_service.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordEncryptionUtil {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER =
            new BCryptPasswordEncoder(12); // strength (10–14 recommended)

    private PasswordEncryptionUtil() {
        // prevent instantiation
    }

    /**
     * Encrypt (hash + salt) plain password
     */
    public static String encryptPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return PASSWORD_ENCODER.encode(plainPassword);
    }

    /**
     * Verify plain password against stored hash
     */
    public static boolean matches(String plainPassword, String hashedPassword) {
        return PASSWORD_ENCODER.matches(plainPassword, hashedPassword);
    }
}
