package com.emis.auth_service.controller;

import com.emis.auth_service.services.impl.UserRoleServiceImpl;
import com.emis.auth_service.dto.request.RoleCreateRequest;
import com.emis.auth_service.dto.response.RoleListResponse;
import com.emis.auth_service.dto.response.RoleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/roles")
public class UserRoleController {

    private final UserRoleServiceImpl roleService;

    public UserRoleController(UserRoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    /**
     * GET /api/v1/iam/roles
     * Returns metadata + roles list.
     */
    @GetMapping
    public ResponseEntity<RoleListResponse> getRoles() {
        RoleListResponse response = roleService.getAllRolesWithStats();
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/iam/roles
     * Creates a role and returns created role as in your example.
     */
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleCreateRequest request) {
        RoleResponse created = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
