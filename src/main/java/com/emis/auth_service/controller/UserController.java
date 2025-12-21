package com.emis.auth_service.controller;

import com.emis.auth_service.configurations.KeycloakProperties;
import com.emis.auth_service.provider.keycloak.client.KeycloakClient;
import com.emis.auth_service.provider.keycloak.client.response.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private KeycloakProperties keycloakProperties;

    @Autowired
    private KeycloakClient keycloakClient;

    @PostMapping("/token")
    public KeycloakTokenResponse generateToken() {
        try {
            KeycloakTokenResponse  keycloakTokenResponse=  keycloakClient.getToken(
                    keycloakProperties.realmName(),
                    keycloakProperties.webClientId(),
                    keycloakProperties.webClientSecret(),
                    keycloakProperties.grantType(),
                    keycloakProperties.adminUsername()
            );
            return keycloakTokenResponse;
        } catch (WebClientRequestException  e) {
            e.printStackTrace();
            return null;
        }
    }
}
