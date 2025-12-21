package com.emis.auth_service.provider.keycloak.client;


import com.emis.auth_service.provider.keycloak.client.response.KeycloakTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
)
public interface KeycloakClient {

    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/token")
    KeycloakTokenResponse getToken(
            @PathVariable String realm,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam String username
    );
}
