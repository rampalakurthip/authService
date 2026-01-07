package com.emis.auth_service.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilObjectsConfigs {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
