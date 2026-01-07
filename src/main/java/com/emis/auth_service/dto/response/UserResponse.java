package com.emis.auth_service.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String staffId;
    private String staffEmail;
    private String fullName;
    private String username;
    private String loginEmail;
    private List<String> roles;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
