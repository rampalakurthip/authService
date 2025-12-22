package com.emis.auth_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum KeycloakErrorCode {
    INVALID_GRANT("invalid_grant"),
    INVALID_CLIENT("invalid_client"),
    INVALID_REQUEST("invalid_request"),
    UNAUTHORIZED_CLIENT("unauthorized_client"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type"),
    INVALID_SCOPE("invalid_scope"),
    ACCOUNT_NOT_SET_UP("Account is not fully set up"),  // Custom parsing
    UNKNOWN("unknown");

    private final String value;

    KeycloakErrorCode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }


  /*  @JsonCreator
    public static KeycloakErrorCode fromValue(String value) {
        if (value == null) return UNKNOWN;
        return switch (value.toLowerCase()) {
            case "invalid_grant" -> INVALID_GRANT;
            case "invalid_client" -> INVALID_CLIENT;
            case "invalid_request" -> INVALID_REQUEST;
            case "unauthorized_client" -> UNAUTHORIZED_CLIENT;
            case "unsupported_grant_type" -> UNSUPPORTED_GRANT_TYPE;
            case "invalid_scope" -> INVALID_SCOPE;
            default -> UNKNOWN;
        };
    }*/
}
