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

    @GetMapping()
    public String home() {
        return "EMIS Auth Service is running.....";
    }
}
