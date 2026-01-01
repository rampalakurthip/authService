package com.emis.auth_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationInfo {
    private int page;
    private int size;
    private long total;
}
