package com.nttdata.creditproducts.service.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final CreditProperties creditProperties;
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl("http://localhost:8086") // URL base del servicio
                .defaultHeader("Content-Type", "application/json")
                .defaultUriVariables(Map.of("accountsUri", creditProperties.getUri()));
    }
}