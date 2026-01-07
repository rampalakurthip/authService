// ResetPasswordRequest.java
package com.emis.auth_service.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String resetToken;
    private String newPassword;
    private String confirmPassword;
}
