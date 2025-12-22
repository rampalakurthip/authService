package com.emis.auth_service.provider.keycloak.client.request;

public record KeycloakUserCreateRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        boolean emailVerified
) {}
