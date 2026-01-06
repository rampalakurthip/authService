package com.emis.auth_service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleCreateRequest(
        @NotNull
        String name,
        String description,
        List<String> modulesAccessible,
        List<String> criticalPermissions,
        String status
) {}