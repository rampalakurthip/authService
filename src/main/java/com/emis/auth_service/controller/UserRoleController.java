package com.emis.auth_service.controller;

import com.emis.auth_service.dto.response.AuthBaseResponse;
import com.emis.auth_service.services.impl.UserRoleServiceImpl;
import com.emis.auth_service.dto.request.RoleCreateRequest;
import com.emis.auth_service.dto.response.RoleListResponse;
import com.emis.auth_service.dto.response.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static com.emis.auth_service.constants.AuthServiceConstants.API_SUCCESS_CODE;
import static com.emis.auth_service.constants.AuthServiceConstants.API_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/api/v1/user/roles")
public class UserRoleController {

    private final UserRoleServiceImpl roleService;

    public UserRoleController(UserRoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    /**
     * GET /api/v1/roles
     * Returns metadata + roles list.
     */
    @GetMapping
    public AuthBaseResponse<RoleListResponse> getRoles() {
        RoleListResponse roleResponse = roleService.getAllRolesWithStats();
        return AuthBaseResponse.<RoleListResponse>builder()
                .data(roleResponse)
                .message(API_SUCCESS_MESSAGE)
                .status(API_SUCCESS_CODE)
                .build();
    }
    /**
     * POST /api/v1/roles
     * Creates a role and returns created role as in your example.
     */
    @PostMapping
    public AuthBaseResponse<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleResponse createdRole = roleService.createRole(request);
        return AuthBaseResponse.<RoleResponse>builder()
                .data(createdRole)
                .message(API_SUCCESS_MESSAGE)
                .status(API_SUCCESS_CODE)
                .build();
    }
    //update role, delete role endpoints can be added similarly

}
