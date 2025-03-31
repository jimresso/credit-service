package com.nttdata.creditproducts.service.configure;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Configuration
public class WebClientConfig {

    @Value("${account.service.url}")
    private String baseUrl;

    @Value("${account.service.uri.put}")
    private String accountsUri;
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl("http://localhost:8086") // URL base del servicio
                .defaultHeader("Content-Type", "application/json")
                .defaultUriVariables(Map.of("accountsUri", accountsUri));
    }
}