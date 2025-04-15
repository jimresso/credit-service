package com.nttdata.creditproducts.service.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "account")
@Getter
@Setter
public class CreditProperties {
    private int transaction;
    private String service;
    private String uri;
}