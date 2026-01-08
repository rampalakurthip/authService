package com.emis.auth_service.controller;

import com.emis.auth_service.dto.request.UserRegisterRequest;
import com.emis.auth_service.dto.response.AuthBaseResponse;
import com.emis.auth_service.dto.response.UserListResponse;
import com.emis.auth_service.dto.response.UserResponse;
import com.emis.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public AuthBaseResponse<UserListResponse> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        UserListResponse response = userService.getUsers(search, role, status, pageable);
        return AuthBaseResponse.<UserListResponse>builder()
                .data(response)
                .message("Users retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping
    public AuthBaseResponse<UserResponse> registerUser(@RequestBody UserRegisterRequest request) {
        UserResponse created = userService.registerUser(request);
        return AuthBaseResponse.<UserResponse>builder()
                .data(created)
                .message("User registered successfully")
                .status(HttpStatus.CREATED.value())
                .build();
    }

    @GetMapping("/me")
    public AuthBaseResponse<UserResponse> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        var res = userService.findByKeycloakUserId(authHeader);
        return AuthBaseResponse.<UserResponse>builder()
                .data(res)
                .message("User retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }
}

