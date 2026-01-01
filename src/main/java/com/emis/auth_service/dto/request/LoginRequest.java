// LoginRequest.java
package com.emis.auth_service.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrEmail;
    private String password;
}
