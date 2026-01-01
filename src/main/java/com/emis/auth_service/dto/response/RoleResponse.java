package com.emis.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private String id;
    private String name;
    private String description;
    private List<String> modulesAccessible;
    private List<String> criticalPermissions;
    private String status;
    private Instant createdAt;

}
