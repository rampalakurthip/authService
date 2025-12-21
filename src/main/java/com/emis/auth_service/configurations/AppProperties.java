package com.emis.auth_service.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Urls urls
) {
    public record Urls(
            String url
    ) {}
}
