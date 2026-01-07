package com.emis.auth_service.provider.keycloak;

import com.emis.auth_service.configurations.KeycloakProperties;
import com.emis.auth_service.provider.keycloak.client.KeycloakClient;
import com.emis.auth_service.provider.keycloak.client.request.KeycloakUserCreateRequest;
import com.emis.auth_service.provider.keycloak.client.response.KeycloakErrorResponse;
import com.emis.auth_service.provider.keycloak.client.response.KeycloakTokenResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthProvider {

    private final KeycloakClient keycloakClient;
    private final KeycloakProperties keycloakProperties;
    private final ObjectMapper objectMapper;

    public String createUserInKeycloak(KeycloakUserCreateRequest kcUser) {
        try {
            // 1) Get admin token
            ResponseEntity<Object> tokenRes = keycloakClient.getToken(
                    keycloakProperties.realmName(),
                    keycloakProperties.adminClientId(),
                    keycloakProperties.adminClientSecret(),
                    keycloakProperties.grantType(),
                    keycloakProperties.adminUsername()
            );

            if (!tokenRes.getStatusCode().is2xxSuccessful() || tokenRes.getBody() == null) {
                throw new ResponseStatusException(UNAUTHORIZED, "Failed to get Keycloak admin token");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            KeycloakTokenResponse token = objectMapper.convertValue(
                    tokenRes.getBody(),
                    KeycloakTokenResponse.class
            );

            String adminBearer = "Bearer " + token.accessToken();

            // 2) Create user in Keycloak
            ResponseEntity<Object> createRes = keycloakClient.createUser(
                    keycloakProperties.realmName(),
                    adminBearer,
                    kcUser
            );

            if (!createRes.getStatusCode().is2xxSuccessful() &&
                createRes.getStatusCode().value() != 201) {
                log.error("Keycloak createUser failed: status={}", createRes.getStatusCode());
                throw new ResponseStatusException(UNAUTHORIZED, "Failed to create user in Keycloak");
            }

            // 3) Extract UID from Location header
            URI location = createRes.getHeaders().getLocation();
            if (location == null) {
                throw new ResponseStatusException(UNAUTHORIZED, "Missing Location header from Keycloak");
            }

            // Location: /admin/realms/{realm}/users/{id}
            String path = location.getPath();
            String keycloakId = path.substring(path.lastIndexOf('/') + 1);
            log.info("Keycloak user created with id={}", keycloakId);

            return keycloakId;

        } catch (HttpClientErrorException ex) {
            log.error("Keycloak token/create user failed: status={}, body={}",
                    ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ResponseStatusException(UNAUTHORIZED, "Keycloak error: " + ex.getStatusCode());
        }
    }
    /**
     * validate token and Extract Keycloak userId from JWT access token
     */
    public String validateTokenGetUid(String accessToken) {
        try {
            // Introspect token using web client credentials
            ResponseEntity<Object> response = keycloakClient.introspectToken(
                    keycloakProperties.realmName(),
                    keycloakProperties.webClientId(),
                    keycloakProperties.webClientSecret(),
                    accessToken
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid token"
                );
            }

            // Parse introspection response
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> tokenInfo = objectMapper.convertValue(
                    response.getBody(), new TypeReference<Map<String, Object>>() {}
            );

            if (Boolean.FALSE.equals(tokenInfo.get("active"))) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Token expired or inactive"
                );
            }

            // Extract user ID (sub claim)
            String userId = (String) tokenInfo.get("sub");

            if (userId == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Token missing user ID (sub)"
                );
            }

            return userId;
        } catch (Exception e) {
            log.error("Token introspection failed", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token validation failed");
        }
    }

    /**
     * Generate user access token (Direct Password Grant)
     */
    public KeycloakTokenResponse generateToken(String userName) {
        KeycloakTokenResponse response;

        try {

            // Direct password grant - web client only
            ResponseEntity<Object> tokenRes = keycloakClient.getToken(
                    keycloakProperties.realmName(),
                    keycloakProperties.webClientId(),
                    keycloakProperties.webClientSecret(),
                    keycloakProperties.grantType(),
                    userName
            );

            if (!tokenRes.getStatusCode().is2xxSuccessful()) {
                KeycloakErrorResponse error = objectMapper.convertValue(
                        tokenRes.getBody(),
                        KeycloakErrorResponse.class
                );
                log.error("Token generation failed: {}", error.errorDescription());
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        error.errorDescription() != null ?
                                error.errorDescription() : "Invalid credentials"
                );
            }

            response = objectMapper.convertValue(
                    tokenRes.getBody(),
                    KeycloakTokenResponse.class
            );

            log.info("Token generated for: {}", userName);

            return response;

        } catch (HttpClientErrorException.Unauthorized ex) {
            log.error("Invalid credentials: {}", ex.getResponseBodyAsString());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        } catch (HttpStatusCodeException ex) {
            log.error("Keycloak error {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ResponseStatusException(ex.getStatusCode(), "Token service error");
        } catch (Exception ex) {
            log.error("Token generation failed", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to generate token"
            );
        }
    }
    /**
     * Refresh access token using refresh_token
     */
    public KeycloakTokenResponse refreshToken(String refreshToken) {
        KeycloakTokenResponse response;

        try {
            log.debug("Refreshing token for realm: {}", keycloakProperties.realmName());

            ResponseEntity<Object> tokenRes = keycloakClient.refreshToken(
                    keycloakProperties.realmName(),
                    keycloakProperties.webClientId(),
                    keycloakProperties.webClientSecret(),
                    "refresh_token",                 // grant_type=refresh_token
                    refreshToken                     // refresh_token
            );

            if (!tokenRes.getStatusCode().is2xxSuccessful()) {
                KeycloakErrorResponse error = objectMapper.convertValue(
                        tokenRes.getBody(),
                        KeycloakErrorResponse.class
                );
                log.error("Refresh failed: {}", error.errorDescription());

                // 400 = invalid/expired refresh token
                if (tokenRes.getStatusCode().value() == 400) {
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Refresh token expired or invalid"
                    );
                }
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        error.errorDescription()
                );
            }

            response = objectMapper.convertValue(
                    tokenRes.getBody(),
                    KeycloakTokenResponse.class
            );

            log.info("Token refreshed successfully (new expiry: {})",
                    Instant.ofEpochSecond(response.expiresIn())
            );

            return response;

        } catch (HttpClientErrorException.Unauthorized ex) {
            log.error("Refresh token invalid: {}", ex.getResponseBodyAsString());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Refresh token invalid or expired"
            );
        } catch (HttpStatusCodeException ex) {
            log.error("Refresh error {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ResponseStatusException(ex.getStatusCode(), "Refresh failed");
        } catch (Exception ex) {
            log.error("Unexpected refresh error", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Token refresh failed"
            );
        }
    }

}


