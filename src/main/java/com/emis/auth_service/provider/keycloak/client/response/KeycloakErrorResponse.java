package com.emis.auth_service.provider.keycloak.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakErrorResponse(
        String error,
        @JsonProperty("error_description")
        String errorDescription,
        //added custom field
        Integer statusCode
) {

}
