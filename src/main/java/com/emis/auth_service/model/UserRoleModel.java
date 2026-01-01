package com.emis.auth_service.model;


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
@Table(name = "user_roles")
public class UserRoleModel {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id; // e.g. "role-admin"

    @Column(name = "name", nullable = false, unique = true, length = 150)
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
