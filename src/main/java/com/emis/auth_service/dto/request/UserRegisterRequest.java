package com.emis.auth_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String staffId;
    private String staffEmail;
    private String username;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String loginEmail;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, digit and special character"
    )
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private List<String> roles;
    private String status;
}
