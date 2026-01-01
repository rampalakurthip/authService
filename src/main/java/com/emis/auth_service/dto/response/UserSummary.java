package com.emis.auth_service.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummary {
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private LocalDateTime lastUpdated;
}
