package com.emis.auth_service.services;

import com.emis.auth_service.dto.request.UserRegisterRequest;
import com.emis.auth_service.dto.response.UserListResponse;
import com.emis.auth_service.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
    UserListResponse getUsers(String search, String role, String status, Pageable pageable);
}
