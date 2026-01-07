package com.emis.auth_service.configurations;

import com.emis.auth_service.provider.keycloak.client.KeycloakClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(
        basePackages = "com.emis.auth_service.provider.keycloak.client",
        types = { KeycloakClient.class },
        group = "keycloak-client"
)
public class HttpClientConfigs {

}
