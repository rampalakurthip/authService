package com.emis.auth_service.services.impl;

import com.emis.auth_service.dto.request.RoleCreateRequest;
import com.emis.auth_service.dto.response.RoleListResponse;
import com.emis.auth_service.dto.response.RoleResponse;
import com.emis.auth_service.model.UserRoleModel;
import com.emis.auth_service.repository.UserRoleRepository;
import com.emis.auth_service.services.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository roleRepository;

    public UserRoleServiceImpl(UserRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleResponse createRole(RoleCreateRequest request) {
        UserRoleModel role = new UserRoleModel();

        // Generate ID – you can adjust this to your convention
        String generatedId = "role-" + request.name()
                .toLowerCase()
                .replaceAll("\\s+", "-");

        role.setId(generatedId);
        role.setName(request.name());
        role.setDescription(request.description());
        role.setModulesAccessible(
                request.modulesAccessible() != null ? request.modulesAccessible() : List.of()
        );
        role.setCriticalPermissions(
                request.criticalPermissions() != null ? request.criticalPermissions() : List.of()
        );
        role.setStatus(request.status() != null ? request.status() : "ACTIVE");
        role.setCreatedAt(Instant.now());
        role.setLastModifiedAt(Instant.now());

        UserRoleModel saved = roleRepository.save(role);
        return toRoleResponse(saved);
    }

    @Transactional(readOnly = true)
    public RoleListResponse getAllRolesWithStats() {
        List<UserRoleModel> all = roleRepository.findAll();

        RoleListResponse response = new RoleListResponse();
        response.setTotalRoles(all.size());

        // Placeholder: plug in actual active user count when user service is ready
        response.setActiveUsers(23L); // hard-coded to match your example for now

        // lastModified = max(lastModifiedAt)
        Instant lastModified = all.stream()
                .map(UserRoleModel::getLastModifiedAt)
                .filter(i -> i != null)
                .max(Comparator.naturalOrder())
                .orElse(null);

        response.setLastModified(lastModified);

        List<RoleResponse> roleResponses = all.stream()
                .map(this::toRoleResponse)
                .toList();
        response.setRoles(roleResponses);

        return response;
    }

    private RoleResponse toRoleResponse(UserRoleModel role) {
        RoleResponse dto = new RoleResponse();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setModulesAccessible(role.getModulesAccessible());
        dto.setCriticalPermissions(role.getCriticalPermissions());
        dto.setStatus(role.getStatus());
        dto.setCreatedAt(role.getCreatedAt());
        return dto;
    }
}
