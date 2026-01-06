package com.emis.auth_service.services.impl;

import com.emis.auth_service.dto.request.RoleCreateRequest;
import com.emis.auth_service.dto.response.RoleListResponse;
import com.emis.auth_service.dto.response.RoleResponse;
import com.emis.auth_service.enums.UserRole;
import com.emis.auth_service.exceptions.SomethingWentWrongException;
import com.emis.auth_service.model.UserRoleModel;
import com.emis.auth_service.repository.UserRepository;
import com.emis.auth_service.repository.UserRoleRepository;
import com.emis.auth_service.services.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository roleRepository;
    private final UserRepository userRepository;

    public UserRoleServiceImpl(UserRoleRepository roleRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public RoleResponse createRole(RoleCreateRequest request) {
        UserRoleModel role = new UserRoleModel();
        // roleCode generation for future use
        String roleCode = request.name()
                .toUpperCase()
                .replaceAll("\\s+", "-");
        role.setRoleCode(UserRole.fromLabel(request.name()));
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
        log.info("Fetching all user roles with stats");
        try {
            List<UserRoleModel> all = roleRepository.findAll();
            RoleListResponse response = new RoleListResponse();
            response.setTotalRoles(all.size());

            // ✅ REAL activeUsers logic using UserRepository
            response.setActiveUsers(userRepository.countActiveUsers());

            // lastModified = max(lastModifiedAt)
            Instant lastModified = all.stream()
                    .map(UserRoleModel::getLastModifiedAt)
                    .filter(Objects::nonNull)
                    .max(Comparator.naturalOrder())
                    .orElse(Instant.now());

            response.setLastModified(lastModified);
            List<RoleResponse> roleResponses = all.stream()
                    .map(this::toRoleResponse)
                    .toList();
            response.setRoles(roleResponses);
            return response;

        } catch (Exception e) {
            throw  new SomethingWentWrongException("GET:/api/v1/user/roles", e.getMessage());
        }

    }

    private RoleResponse toRoleResponse(UserRoleModel role) {
        RoleResponse dto = new RoleResponse();
        dto.setId(role.getId());
        dto.setRoleCode(role.getRoleCode().name());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setModulesAccessible(role.getModulesAccessible());
        dto.setCriticalPermissions(role.getCriticalPermissions());
        dto.setStatus(role.getStatus());
        dto.setCreatedAt(role.getCreatedAt());
        return dto;
    }
}
