package com.emis.auth_service.constants;

import jakarta.persistence.criteria.CriteriaBuilder;

public class AuthServiceConstants {

    private AuthServiceConstants() {
        // Private constructor to prevent instantiation
    }
    public static final String API_SUCCESS_MESSAGE = "success";
    public static final String API_FAILED_MESSAGE = "failed";
    public static final Integer API_SUCCESS_CODE = 200;
    public static final String AUTH_SERVICE = "AUTH-SERVICE";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String INVALID_TOKEN = "Invalid or expired token";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
}
