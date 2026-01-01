package com.emis.auth_service.dto.request;

import java.util.List;

public record RoleCreateRequest(
        String name,
        String description,
        List<String> modulesAccessible,
        List<String> criticalPermissions,
        String status
) {}