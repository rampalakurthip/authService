package com.emis.auth_service.utils;

import com.emis.auth_service.dto.response.UserContextDTO;
import com.emis.auth_service.enums.UserRole;
import com.emis.auth_service.model.UserModel;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static UserModel getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserModel user)) {
            throw new IllegalStateException("Invalid security context");
        }
        return user;
    }

    public static UserContextDTO getUserContext() {
        UserModel user = getCurrentUser();

        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::name)
                .collect(Collectors.toSet());

        return UserContextDTO.builder()
                // Core Identity
                .userId(user.getUserId())

                // Business Identifiers
                .staffId(user.getStaffId())
                .username(user.getUsername())
                .loginEmail(user.getLoginEmail())
                .staffEmail(user.getStaffEmail())

                // Personal Info
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .mobileNumber(user.getMobileNumber())

                // Status & Audit
                .status(user.getStatus())
                .isActive(user.getIsActive())
                .emailVerified(user.getEmailVerified())
                .mobileVerified(user.getMobileVerified())

                // Roles & Permissions
                .roles(roles)
                .attributes(Map.of()) // extend later if needed

                // Audit fields
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())

                // Token context (optional)
                .sessionId(null)       // can be filled from token if needed
                .tokenExpiry(null)     // can be filled from token introspection

                .build();
    }
}
