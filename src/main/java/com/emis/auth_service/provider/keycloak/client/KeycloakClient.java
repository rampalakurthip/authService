package com.emis.auth_service.provider.keycloak.client;


import com.emis.auth_service.provider.keycloak.client.request.KeycloakUserCreateRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        headers = "Accept=application/json",
        method = "Content-Type=application/json"
)
public interface KeycloakClient {

    //admin token generation
    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Object> getToken(
            @PathVariable String realm,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam String username
    );

    //add user in keycloak realm
    @PostExchange(value = "/admin/realms/{realm}/users")
    ResponseEntity<Object> createUser(
            @PathVariable String realm,
            @RequestHeader("Authorization") String adminToken,
            @RequestBody KeycloakUserCreateRequest userData
    );
 // validate token
    @PostExchange(
            value = "/realms/{realm}/protocol/openid-connect/token/introspect",
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<Object> introspectToken(
            @PathVariable String realm,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("token") String token
    );

    @PostExchange(
            value = "/realms/{realm}/protocol/openid-connect/token",
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<Object> refreshToken(
            @PathVariable String realm,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,   // use "refresh_token"
            @RequestParam("refresh_token") String refreshToken
    );
    @PostExchange(
            value = "/realms/{realm}/protocol/openid-connect/logout",
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<Object> logout(
            @PathVariable String realm,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("refresh_token") String refreshToken
    );


}
