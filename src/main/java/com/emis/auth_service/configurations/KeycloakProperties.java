package com.emis.auth_service.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String baseUrl,
        String realmName,
        String grantType,
        String adminClientId,
        String adminUsername,
        String webClientId,
        String webClientSecret
) {
}
