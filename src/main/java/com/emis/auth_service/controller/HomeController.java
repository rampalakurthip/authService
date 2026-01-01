package com.emis.auth_service.controller;

import com.emis.auth_service.configurations.KeycloakProperties;
import com.emis.auth_service.provider.keycloak.client.KeycloakClient;
import com.emis.auth_service.provider.keycloak.client.request.KeycloakUserCreateRequest;
import com.emis.auth_service.provider.keycloak.client.response.KeycloakErrorResponse;
import com.emis.auth_service.provider.keycloak.client.response.KeycloakTokenResponse;
import com.emis.auth_service.utils.Authorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RestController()
@RequestMapping()
public class HomeController {

    @Autowired
    private KeycloakProperties keycloakProperties;

    @Autowired
    private KeycloakClient keycloakClient;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/token")
    public KeycloakTokenResponse generateToken(@RequestBody() KeycloakUserCreateRequest keycloakUserCreateRequest) {
        ResponseEntity<Object> res;

        try {
            res = keycloakClient.getToken(
                    keycloakProperties.realmName(),
                    keycloakProperties.webClientId(),
                    keycloakProperties.webClientSecret(),
                    keycloakProperties.grantType(),
                    keycloakProperties.adminUsername()
            );
            KeycloakTokenResponse response = null;
            if (res.getStatusCode().is2xxSuccessful()) {
                 response =  objectMapper.convertValue(
                        res.getBody(),
                        KeycloakTokenResponse.class
                );
            }
            keycloakClient.createUser(keycloakProperties.realmName(),"Bearer "+response.accessToken(),keycloakUserCreateRequest);
        } catch (HttpClientErrorException ex) {
            log.error("Keycloak token request failed: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAs(KeycloakErrorResponse.class));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials or token");
        }
        return null;
    }




    @GetMapping()
    @Authorize()
    public String home() {
        return "EMIS Auth Service is running.....";
    }
}
