package com.emis.auth_service;

import com.emis.auth_service.configurations.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*
@Author: @RamachandramP
This is Auth services helps integrate with other services for authentication and authorization.
It is connected to keycloak for generating  token and maintain sessions.
 */

@SpringBootApplication
@ConfigurationPropertiesScan
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
