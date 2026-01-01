package com.emis.auth_service.services;

import com.emis.auth_service.dto.request.RoleCreateRequest;
import com.emis.auth_service.dto.response.RoleListResponse;
import com.emis.auth_service.dto.response.RoleResponse;

public interface UserRoleService {

    RoleResponse createRole(RoleCreateRequest request);

    RoleListResponse getAllRolesWithStats();
}
