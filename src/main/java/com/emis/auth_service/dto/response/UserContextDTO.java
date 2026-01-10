package com.emis.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContextDTO {
    
    // Core Identity (Keycloak userId)
    private String userId;           // Keycloak UUID (PK)
    
    // Business Identifiers
    private String staffId;
    private String username;
    private String loginEmail;
    private String staffEmail;
    
    // Personal Info
    private String firstName;
    private String lastName;
    private String fullName;
    private String mobileNumber;
    
    // Status & Audit
    private String status;           // ACTIVE, INACTIVE, SUSPENDED
    private Boolean isActive;
    private Boolean emailVerified;
    private Boolean mobileVerified;
    
    // Roles & Permissions (JSON/String for other services)
    private Set<String> roles;       // ["USER", "ADMIN", "TEACHER"]
    private Map<String, Object> attributes;  // Additional claims
    
    // Audit Fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // Token Context (optional)
    private String sessionId;
    private Long tokenExpiry;
}
