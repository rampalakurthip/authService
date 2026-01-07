package com.emis.auth_service.services.impl;

import com.emis.auth_service.dto.request.UserRegisterRequest;
import com.emis.auth_service.enums.UserRole;
import com.emis.auth_service.model.UserModel;
import com.emis.auth_service.provider.keycloak.KeycloakAuthProvider;
import com.emis.auth_service.provider.keycloak.client.KeycloakClient;
import com.emis.auth_service.provider.keycloak.client.request.KeycloakUserCreateRequest;
import com.emis.auth_service.repository.UserRepository;
import com.emis.auth_service.dto.response.PaginationInfo;
import com.emis.auth_service.dto.response.UserListResponse;
import com.emis.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.emis.auth_service.dto.response.UserResponse;
import com.emis.auth_service.dto.response.UserSummary;
import org.springframework.web.bind.annotation.RequestHeader;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakAuthProvider keycloakAuthProvider;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check uniqueness
        if (userRepository.findByUsernameIgnoreCase(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByLoginEmailIgnoreCase(request.getLoginEmail()).isPresent()) {
            throw new IllegalArgumentException("Login email already exists");
        }
        if (userRepository.findByStaffId(request.getStaffId()).isPresent()) {
            throw new IllegalArgumentException("Staff ID already exists");
        }
        if (userRepository.findByStaffEmailIgnoreCase(request.getStaffEmail()).isPresent()) {
            throw new IllegalArgumentException("Staff email already exists");
        }
        KeycloakUserCreateRequest kcUser = toKeycloakUser(request);
        String keycloakUserId = keycloakAuthProvider.createUserInKeycloak(kcUser);

        UserModel user = UserModel.builder()
                .userId(keycloakUserId)//  uuid jwt token sid
                .staffId(request.getStaffId())
                .staffEmail(request.getStaffEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFirstName() + " " + request.getLastName())
                .username(request.getUsername())
                .loginEmail(request.getLoginEmail())
                .emailVerified(StringUtils.isNotEmpty(request.getLoginEmail()))
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .mobileNumber(request.getMobileNumber())
                .roles(UserRole.fromLabels(request.getRoles()))
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .isActive(true)
                .build();

        UserModel saved = userRepository.save(user);
        return toUserResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserListResponse getUsers(String search, String roleCode, String status, Pageable pageable) {
        UserRole roleEnum = null;
        if (StringUtils.isNotEmpty(roleCode)) {
            roleEnum = UserRole.fromCode(roleCode);
        }
        Page<UserModel> usersPage = userRepository.findUsersWithFilters(search, roleEnum, status, pageable);

        UserListResponse response = new UserListResponse();
        
        // Summary
        UserSummary summary = UserSummary.builder()
                .totalUsers(usersPage.getTotalElements())
                .activeUsers(countActiveUsers())
                .inactiveUsers(usersPage.getTotalElements() - countActiveUsers())
                .lastUpdated(LocalDateTime.now())
                .build();
        response.setSummary(summary);

        // Users
        List<UserResponse> userResponses = usersPage.getContent().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
        response.setUsers(userResponses);

        // Pagination
        PaginationInfo pagination = PaginationInfo.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .total(usersPage.getTotalElements())
                .build();
        response.setPagination(pagination);

        return response;
    }

    @Override
    public UserResponse findByKeycloakUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        // 1) Get Keycloak userId from token
        String keycloakUserId = keycloakAuthProvider.validateTokenGetUid(token);
        return toUserResponse(userRepository.findByUserId(keycloakUserId) // ✅ String PK
                .orElseThrow(() -> new InternalAuthenticationServiceException(
                        "User not found for Keycloak ID: " + keycloakUserId
                )));

    }

    private UserResponse toUserResponse(UserModel user) {
        return UserResponse.builder()
                .id(user.getUserId())
                .staffId(user.getStaffId())
                .staffEmail(user.getStaffEmail())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .loginEmail(user.getLoginEmail())
                .roles(UserRole.toLabels(user.getRoles()))
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    private long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
    private KeycloakUserCreateRequest toKeycloakUser(UserRegisterRequest request) {
        return KeycloakUserCreateRequest.builder()
                .username(request.getUsername())
                .email(request.getLoginEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .emailVerified(true)
                .build();
    }

}
