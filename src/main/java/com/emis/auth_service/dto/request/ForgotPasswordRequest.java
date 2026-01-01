// ForgotPasswordRequest.java
package com.emis.auth_service.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String usernameOrEmail;
}
