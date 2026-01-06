package com.emis.auth_service.model;

import com.emis.auth_service.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "auth_service_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uq_users_login_email", columnNames = "login_email"),
                @UniqueConstraint(name = "uq_users_staff_id", columnNames = "staff_id"),
                @UniqueConstraint(name = "uq_users_mobile", columnNames = "mobile_number")
        },
        indexes = {
                @Index(name = "idx_users_status", columnList = "status"),
                @Index(name = "idx_users_staff_id", columnList = "staff_id")
        }
)
public class UserModel {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    /* ===================== Business Identifiers ===================== */
    @Column(name = "staff_id", length = 50, unique = true)
    private String staffId;

    @Column(name = "staff_email", length = 50, unique = true)
    private String staffEmail;

    /* ===================== Core Credentials ===================== */
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "login_email", nullable = false, length = 255)
    private String loginEmail;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /* ===================== Profile ===================== */
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "full_name")
    private String fullName; // Computed: firstName + lastName

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    /* ===================== Roles & Permissions ===================== */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 50, nullable = false)
    private List<UserRole> roles = new ArrayList<>();

    /* ===================== Status & Audit ===================== */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, SUSPENDED, LOCKED

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /* ===================== Password Management ===================== */
    @CreationTimestamp
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    /* ===================== Timestamps ===================== */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "failed_login_attempts" )
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "mobile_verified")
    private Boolean mobileVerified = false;

    /* ===================== Metadata ===================== */
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
