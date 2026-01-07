// ResetPasswordResponse.java
package com.emis.auth_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordResponse {
    private String message;
    private boolean passwordUpdated;
}
