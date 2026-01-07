package com.emis.auth_service.provider.keycloak.client.request;

import lombok.Builder;
import lombok.Data;


@Builder
public record KeycloakUserCreateRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        boolean emailVerified
) {}
