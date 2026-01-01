package com.emis.auth_service.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListResponse {
    private UserSummary summary;
    private List<UserResponse> users;
    private PaginationInfo pagination;
}
