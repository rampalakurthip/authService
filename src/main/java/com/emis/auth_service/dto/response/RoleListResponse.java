package com.emis.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleListResponse {

    private long totalRoles;
    private long activeUsers;       // you will fill this from user service or another source
    private Instant lastModified;
    private List<RoleResponse> roles;

}
