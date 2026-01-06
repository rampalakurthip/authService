package com.emis.auth_service.model;


import com.emis.auth_service.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_service_user_roles")
public class UserRoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment integer
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private UserRole roleCode;

    @Column(name = "name", nullable = false ,length = 150)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role_modules",
            joinColumns = @JoinColumn(name = "role_id")
    )
    @Column(name = "module_name", nullable = false)
    private List<String> modulesAccessible = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role_critical_permissions",
            joinColumns = @JoinColumn(name = "role_id")
    )
    @Column(name = "permission_name", nullable = false)
    private List<String> criticalPermissions = new ArrayList<>();

    @Column(name = "status", nullable = false, length = 30)
    private String status; // e.g. "ACTIVE", "INACTIVE"

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "last_modified_at", nullable = false)
    private Instant lastModifiedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedAt = Instant.now();
    }

}
