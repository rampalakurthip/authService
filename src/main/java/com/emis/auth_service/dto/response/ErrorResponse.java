// ErrorResponse.java
package com.emis.auth_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private String message;
}
